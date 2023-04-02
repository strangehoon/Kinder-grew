package com.sparta.finalproject.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.finalproject.domain.jwt.JwtUtil;
import com.sparta.finalproject.domain.user.dto.KakaoUserRequestDto;
import com.sparta.finalproject.domain.user.dto.ParentSignupRequestDto;
import com.sparta.finalproject.domain.user.dto.TeacherSignupRequestDto;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.domain.user.repository.UserRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import com.sparta.finalproject.global.response.CustomStatusCode;
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
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final S3Service s3Service;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public GlobalResponseDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        String accessToken = getToken(code);

        KakaoUserRequestDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        String createToken = jwtUtil.createToken(kakaoUser.getName(), kakaoUser.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        if(kakaoUser.getName() == null || kakaoUser.getPhoneNumber() == null) {

           return GlobalResponseDto.of(CustomStatusCode.ESSENTIAI_INFO_EMPTY, kakaoUser.getName());
        }

        return GlobalResponseDto.of(CustomStatusCode.ESSENTIAI_INFO_EXIST, kakaoUser.getRole());
    }

    private String getToken(String code) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "");
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

        return KakaoUserRequestDto.builder()
                .kakaoId(kakaoId)
                .name(name)
                .build();

    }

    private User registerKakaoUserIfNeeded(KakaoUserRequestDto kakaoUserRequestDto) {

        Long kakaoId = kakaoUserRequestDto.getKakaoId();
        User kakaoUser = userRepository.findBykakaoId(kakaoId).orElse(null);

        if(kakaoUser == null) {

            UserRoleEnum role = UserRoleEnum.EARLY_USER;
            String name = kakaoUserRequestDto.getName();

            kakaoUser = User.builder()
                            .kakaoId(kakaoId)
                            .name(name)
                            .role(role)
                            .build();

            userRepository.saveAndFlush(kakaoUser);
        }

        return kakaoUser;
    }

    @Transactional
    public GlobalResponseDto parentSignup(ParentSignupRequestDto requestDto, User user) throws IOException {

        String name = requestDto.getName();
        String phoneNumbe = requestDto.getPhoneNumber();
        UserRoleEnum role = UserRoleEnum.USER;
        String profileImageUrl = null;

        if(requestDto.getProfileImage() != null) {

            profileImageUrl = s3Service.upload(requestDto.getProfileImage(), "profile-image");
        }

        String relationship = requestDto.getRelationship();
        String emergencyPhoneNumber = requestDto.getEmergencyPhoneNumber();

        user.update(name, phoneNumbe, role, profileImageUrl, relationship, emergencyPhoneNumber);

        userRepository.saveAndFlush(user);

        return GlobalResponseDto.of(CustomStatusCode.FINAL_SIGNUP_PARENT);
    }

    @Transactional
    public GlobalResponseDto teacherSignup(TeacherSignupRequestDto requestDto, User user) throws IOException{

        if (!ADMIN_TOKEN.equals(requestDto.getADMIN_TOKEN())) {

            return GlobalResponseDto.of(CustomStatusCode.DIFFRENT_ADMIN_TOKEN);
        }

        String name = requestDto.getName();
        String phoneNumber = requestDto.getPhoneNumber();
        UserRoleEnum role = UserRoleEnum.ADMIN;
        LocalDate birthday = requestDto.getBirthday();
        String profileImageUrl = null;

        if(requestDto.getProfileImage() != null) {

            profileImageUrl = s3Service.upload(requestDto.getProfileImage(), "profile-image");
        }

        String resolution = requestDto.getResolution();

        user.update(name, phoneNumber, role , profileImageUrl, birthday, resolution);

        userRepository.saveAndFlush(user);

        return GlobalResponseDto.of(CustomStatusCode.FINAL_SIGNUP_TEACHER);
    }
}
