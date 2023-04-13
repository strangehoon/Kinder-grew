package com.sparta.finalproject.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.finalproject.domain.child.dto.SidebarChildrenInfo;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.jwt.JwtUtil;
import com.sparta.finalproject.domain.kindergarten.dto.KindergartenResponseDto;
import com.sparta.finalproject.domain.user.dto.*;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.domain.user.repository.UserRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.UserException;
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.sparta.finalproject.global.enumType.UserRoleEnum.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final S3Service s3Service;

    private final Classroom classroom;

    private final Child child;

    private final ChildRepository childRepository;

    @Transactional
    public GlobalResponseDto loginUser(String code, HttpServletResponse response) throws JsonProcessingException {

        String accessToken = getToken(code);

        KakaoUserRequestDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
        kakaoUser.putAccessToken(accessToken);
        String createToken = jwtUtil.createToken(kakaoUser.getName(), kakaoUser.getRole());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);


        if(EARLY_USER.equals(kakaoUser.getRole())) {

            return GlobalResponseDto.of(CustomStatusCode.ESSENTIAL_INFO_EMPTY, UserResponseDto.of(kakaoUser.getName(), kakaoUser.getProfileImageUrl()));
        }

        if(EARLY_PARENT.equals(kakaoUser.getRole()) || EARLY_TEACHER.equals(kakaoUser.getRole())) {

            return GlobalResponseDto.of(CustomStatusCode.APPROVAL_WAIT, UserResponseDto.of(kakaoUser.getName(), kakaoUser.getProfileImageUrl(), kakaoUser.getKindergarten()));
        }
        
        return GlobalResponseDto.from(CustomStatusCode.ESSENTIAL_INFO_EXIST);
    }

    private String getToken(String code) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "*******************");
        body.add("redirect_uri", "http://localhost:3000/oauth/kakao/callback");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(

                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserRequestDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(

                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long kakaoId = jsonNode.get("id").asLong();
        String name = jsonNode.get("properties")
                .get("nickname").asText();
        String profileImageUrl;

        if (jsonNode.get("properties").get("profile_image") != null) {

            profileImageUrl = jsonNode.get("properties").get("profile_image").asText();
        } else {

            profileImageUrl = "https://hanghaefinals3.s3.ap-northeast-2.amazonaws.com/profile-image/default_profile_image.jpeg";
        }

        return KakaoUserRequestDto.builder()
                .kakaoId(kakaoId)
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    private User registerKakaoUserIfNeeded(KakaoUserRequestDto requestDto) {

        User kakaoUser = userRepository.findBykakaoId(requestDto.getKakaoId()).orElse(null);

        if (kakaoUser == null) {

            kakaoUser = User.builder()

                    .requestDto(requestDto)
                    .role(EARLY_USER)
                    .profileImageUrl(requestDto.getProfileImageUrl())
                    .build();

            userRepository.save(kakaoUser);
        }

        return kakaoUser;
    }

    @Transactional
    public GlobalResponseDto modifyParent(ParentModifyRequestDto requestDto, User user) throws IOException {

        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, EARLY_PARENT, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.of(CustomStatusCode.REQUEST_SIGNUP_SUCCESS, UserResponseDto.of(user.getName(), user.getProfileImageUrl(), user.getKindergarten()));
    }

    @Transactional
    public GlobalResponseDto modifyTeacher(TeacherModifyRequestDto requestDto, User user) throws IOException {

        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, EARLY_TEACHER, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.of(CustomStatusCode.REQUEST_SIGNUP_SUCCESS, UserResponseDto.of(user.getName(), user.getProfileImageUrl(), user.getKindergarten()));
    }

    @Transactional
    public GlobalResponseDto modifyPrincipal(PrincipalModifyRequestDto requestDto, User user) throws IOException{
        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, PRINCIPAL, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.of(CustomStatusCode.INSERT_PRINCIPAL_INFO_SUCCESS, null);

    }

    @Transactional
    public GlobalResponseDto findUserProfile(User user) {
        KindergartenResponseDto kindergartenResponseDto = KindergartenResponseDto.from(user.getKindergarten());
        if(PRINCIPAL.equals(user.getRole())){
            return GlobalResponseDto.of(CustomStatusCode.PROFILE_INFO_GET_SUCCESS,
                    UserInfoResponseDto.of(kindergartenResponseDto, PrincipalProfileResponseDto.from(user)));
        } else if (TEACHER.equals(user.getRole())) {
            return GlobalResponseDto.of(CustomStatusCode.PROFILE_INFO_GET_SUCCESS,
                    UserInfoResponseDto.of(kindergartenResponseDto, TeacherProfileResponseDto.from(user)));
        } else if (PARENT.equals(user.getRole())){

            List<Child> children = childRepository.findAllById(user.getKakaoId());
            List<SidebarChildrenInfo> childList = new ArrayList<>();

            for(Child child : children) {

                childList.add(SidebarChildrenInfo.from(child));
            }

            return GlobalResponseDto.of(CustomStatusCode.PROFILE_INFO_GET_SUCCESS,
                    UserInfoResponseDto.of(kindergartenResponseDto, ParentProfileResponseDto.from(user), childList));
        } else {

            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
    }

    @Transactional
    public GlobalResponseDto modifyParentProfile(ParentModifyRequestDto requestDto, User user) throws IOException {

        if(!PARENT.equals(user.getRole())) {

            throw new UserException(CustomStatusCode.DIFFERENT_ROLE);
        }

        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, PARENT, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.from(CustomStatusCode.PROFILE_INFO_CHANGE_SUCCESS);

    }

    @Transactional
    public GlobalResponseDto modifyTeacherProfile(TeacherProfileModifyRequestDto requestDto, User user) throws IOException {

        if(!TEACHER.equals(user.getRole())) {

            throw new UserException(CustomStatusCode.DIFFERENT_ROLE);
        }

        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.from(CustomStatusCode.PROFILE_INFO_CHANGE_SUCCESS);

    }

    @Transactional
    public GlobalResponseDto findTeacherList(User user) {
        if(!user.getRole().equals(TEACHER) && !user.getRole().equals(PRINCIPAL)){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        List<User> teacherList = userRepository.findAllByRole(UserRoleEnum.TEACHER);
        List<TeacherResponseDto> responseDtoList = teacherList.stream().map(TeacherResponseDto::from).collect(Collectors.toList());
        return GlobalResponseDto.of(CustomStatusCode.FIND_TEACHER_LIST_SUCCESS, responseDtoList);
    }

    //아이 부모 검색
    @Transactional(readOnly = true)
    public GlobalResponseDto findParentByName(String name, User user) {
        List<User> parentList = userRepository.findByRoleAndNameContaining(UserRoleEnum.PARENT, name);
        List<ParentResponseDto> parentResponseDtoList = new ArrayList<>();
        for (User parent : parentList) {
            parentResponseDtoList.add(ParentResponseDto.from(parent));
        }
        return GlobalResponseDto.of(CustomStatusCode.SEARCH_PARENT_SUCCESS, parentResponseDtoList);
    }

    @Transactional
    public GlobalResponseDto findMemberPage(Long kindergartenId, UserRoleEnum userRole, int page, int size, User user) {
        if(!user.getRole().equals(PRINCIPAL)){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> userList;
        List<User> earlyUserList;
        Long memberCount;
        if(userRole.equals(TEACHER)){
            userList = userRepository.findAllByRoleAndKindergartenId(TEACHER, kindergartenId,pageable);
            earlyUserList = userRepository.findAllByRoleAndKindergartenId(EARLY_TEACHER, kindergartenId);
            memberCount = userRepository.countAllByRoleAndKindergartenId(TEACHER, kindergartenId);
        } else {
            userList = userRepository.findAllByRoleAndKindergartenId(PARENT, kindergartenId,pageable);
            earlyUserList = userRepository.findAllByRoleAndKindergartenId(EARLY_PARENT, kindergartenId);
            memberCount = userRepository.countAllByRoleAndKindergartenId(PARENT, kindergartenId);
        }
        List<MemberResponseDto> memberResponseDtoList = userList.stream().map(MemberResponseDto::of).collect(Collectors.toList());
        List<MemberResponseDto> earlyMemberResponseDtoList = earlyUserList.stream().map(MemberResponseDto::of).collect(Collectors.toList());

        return GlobalResponseDto.of(CustomStatusCode.FIND_MEMBER_PAGE_SUCCESS,
                MemberPageResponseDto.of(userRole, memberCount, memberResponseDtoList, earlyMemberResponseDtoList));
    }

    @Transactional
    public GlobalResponseDto authenticateUser(Long requestUserId, User user) {
        if(!user.getRole().equals(PRINCIPAL)){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        User requestUser = userRepository.findById(requestUserId).orElseThrow(
                () -> new UserException(CustomStatusCode.USER_NOT_FOUND)
        );
        if(requestUser.getRole().equals(EARLY_PARENT)){
            requestUser.setRole(PARENT);
        } else if (requestUser.getRole().equals(EARLY_TEACHER)) {
            requestUser.setRole(TEACHER);
        } else {
            throw new UserException(CustomStatusCode.DIFFERENT_ROLE);
        }
        return GlobalResponseDto.of(CustomStatusCode.USER_AUTHORIZED, null);
    }

    @Transactional
    public GlobalResponseDto rejectUser(Long requestUserId, User user) {
        if(!user.getRole().equals(PRINCIPAL)){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        User requestUser = userRepository.findById(requestUserId).orElseThrow(
                () -> new UserException(CustomStatusCode.USER_NOT_FOUND)
        );
        requestUser.clear();
        return GlobalResponseDto.of(CustomStatusCode.USER_REJECTED, null);
    }

    @Transactional
    public GlobalResponseDto removeUser(User user) {

        if(user.getRole().equals(PARENT)) {

            userRepository.delete(user);

        } else if(user.getRole().equals(TEACHER) && !classroom.getClassroomTeacher().getId().equals(user.getId())){

            userRepository.delete(user);
        } else {

            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }

        return GlobalResponseDto.from(CustomStatusCode.REMOVE_SUCCESS);
    }

    private String getProfileImageUrl(CommonGetProfileImageRequestDto requestDto, User user) throws IOException {
        String profileImageUrl;

        if (requestDto.isCancelled()) {
            profileImageUrl = "https://hanghaefinals3.s3.ap-northeast-2.amazonaws.com/profile-image/default_profile_image.jpeg";
        } else {
            profileImageUrl = user.getProfileImageUrl();

            if (requestDto.getProfileImage() != null) {
                profileImageUrl = s3Service.upload(requestDto.getProfileImage(), "profile-image");
            }
        }

        return profileImageUrl;
    }
}
