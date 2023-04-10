package com.sparta.finalproject.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.finalproject.domain.jwt.JwtUtil;
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

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public GlobalResponseDto loginUser(String code, HttpServletResponse response) throws JsonProcessingException {

        String accessToken = getToken(code);

        KakaoUserRequestDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        String createToken = jwtUtil.createToken(kakaoUser.getName(), kakaoUser.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        if (EARLY_USER.equals(kakaoUser.getRole())) {

            return GlobalResponseDto.of(CustomStatusCode.ESSENTIAI_INFO_EMPTY, UserResponseDto.of(kakaoUser.getName(), kakaoUser.getProfileImageUrl()));
        }

        return GlobalResponseDto.from(CustomStatusCode.ESSENTIAI_INFO_EXIST);
    }

    private String getToken(String code) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "24e49a760b59910d2e8dabe86f87e203");
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

        user.update(requestDto, USER, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.of(CustomStatusCode.FINAL_SIGNUP_PARENT, UserResponseDto.of(user.getName(), user.getProfileImageUrl()));
    }

    @Transactional
    public GlobalResponseDto modifyTeacher(TeacherModifyRequestDto requestDto, User user) throws IOException {

        if (!ADMIN_TOKEN.equals(requestDto.getADMIN_TOKEN())) {

            throw new UserException(CustomStatusCode.DIFFRENT_ADMIN_TOKEN);
        }

        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, ADMIN, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.of(CustomStatusCode.FINAL_SIGNUP_TEACHER, UserResponseDto.of(user.getName(), user.getProfileImageUrl()));
    }

    @Transactional
    public GlobalResponseDto modifyPrincipal(PrincipalModifyRequestDto requestDto, User user) throws IOException{
        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, PRINCIPAL, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.of(CustomStatusCode.FINAL_SIGNUP_PRINCIPAL, null);

    }

    @Transactional
    public GlobalResponseDto detailsUserProfile(User user) {

        if(USER.equals(user.getRole())) {

            return GlobalResponseDto.of(CustomStatusCode.PROFILE_INFO_GET_SUCCESS, ParentProfileResponseDto.of(user));

        }

        return GlobalResponseDto.of(CustomStatusCode.PROFILE_INFO_GET_SUCCESS, TeacherProfileResponseDto.from(user));

    }

    @Transactional
    public GlobalResponseDto modifyParentProfile(ParentModifyRequestDto requestDto, User user) throws IOException {

        if(!USER.equals(user.getRole())) {

            throw new UserException(CustomStatusCode.DIFFRENT_ROLE);
        }

        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, USER, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.from(CustomStatusCode.PROFILE_INFO_CHANGE_SUCCESS);

    }

    @Transactional
    public GlobalResponseDto modifyTeacherProfile(TeacherProfileModifyRequestDto requestDto, User user) throws IOException {

        if(!ADMIN.equals(user.getRole())) {

            throw new UserException(CustomStatusCode.DIFFRENT_ROLE);
        }

        String profileImageUrl = getProfileImageUrl(requestDto, user);

        user.update(requestDto, profileImageUrl);

        userRepository.save(user);

        return GlobalResponseDto.from(CustomStatusCode.PROFILE_INFO_CHANGE_SUCCESS);

    }

    @Transactional
    public GlobalResponseDto findTeacherList(User user) {
        if(!user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        List<User> teacherList = userRepository.findAllByRole(UserRoleEnum.ADMIN);
        List<TeacherResponseDto> responseDtoList = teacherList.stream().map(TeacherResponseDto::from).collect(Collectors.toList());
        return GlobalResponseDto.of(CustomStatusCode.FIND_TEACHER_LIST_SUCCESS, responseDtoList);
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

    //아이 부모 검색
    @Transactional
    public GlobalResponseDto findParentByName(String name, User user) {
        List<User> parentList = userRepository.findByRoleAndNameContaining(UserRoleEnum.USER, name);
        List<ParentResponseDto> parentResponseDtoList = new ArrayList<>();
        for (User parent : parentList) {
            parentResponseDtoList.add(ParentResponseDto.from(parent));
        }
        return GlobalResponseDto.of(CustomStatusCode.SEARCH_PARENT_SUCCESS, parentResponseDtoList);
    }
}
