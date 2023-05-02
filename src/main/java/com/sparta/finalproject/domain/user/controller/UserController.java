package com.sparta.finalproject.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.domain.user.dto.*;
import com.sparta.finalproject.domain.user.service.UserService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 소셜 회원 가입 및 로그인
    @CrossOrigin(origins = {"https://front-omega-vert.vercel.app", "http://localhost:3000"}, exposedHeaders = "Authorization")
    @GetMapping("oauth/kakao/callback")
    public GlobalResponseDto userLogin(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        return userService.loginUser(code, request, response);
    }

    // 회원 탈퇴
    @GetMapping("kakao/unlinked")
    public GlobalResponseDto userUnlinked(@AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        return userService.unlinkedUser(userDetails.getUser());
    }

    // 학부모 추가 정보 입력
    @PutMapping("parent/info")
    public GlobalResponseDto parentModify(@Valid @ModelAttribute ParentModifyRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.modifyParent(requestDto, userDetails.getUser());
    }

    // 선생님 추가 정보 입력
    @PutMapping("teacher/info")
    public GlobalResponseDto teacherModify(@Valid @ModelAttribute TeacherModifyRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.modifyTeacher(requestDto, userDetails.getUser());
    }

    // 원장 선생님 추가 정보 입력
    @PutMapping("/principal/info")
    public GlobalResponseDto principalModify(@Valid @ModelAttribute PrincipalModifyRequestDto requestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.modifyPrincipal(requestDto, userDetails.getUser());
    }

    // 사이드바 유저 프로필 조회
    @GetMapping("user/profile")
    public GlobalResponseDto userProfileFind(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.findUserProfile(userDetails.getUser());
    }

    // 학부모 프로필 수정
    @PutMapping("parent/profile")
    public GlobalResponseDto parentProfileModify(@Valid @ModelAttribute ParentModifyRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.modifyParentProfile(requestDto, userDetails.getUser());
    }

    // 선생님 프로필 수정
    @PutMapping("teacher/profile")
    public GlobalResponseDto teacherProfileModify(@Valid @ModelAttribute TeacherProfileModifyRequestDto requestDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.modifyTeacherProfile(requestDto, userDetails.getUser());
    }

    // 선생님 목록 조회
    @GetMapping("kindergarten/{kindergartenId}/teacher")
    public GlobalResponseDto teacherListFind(@PathVariable Long kindergartenId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.findTeacherList(kindergartenId, userDetails.getUser());
    }

    // 부모 검색
    @GetMapping("search/parent")
    public GlobalResponseDto parentFindByName(@RequestParam String name, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.findParentByName(name,userDetails.getUser());
    }

    // 가입 승인
    @PutMapping("user/{userId}/authenticate")
    public GlobalResponseDto userAuthenticate(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.authenticateUser(userId, userDetails.getUser());
    }

    // 가입 거절
    @DeleteMapping("user/{userId}/authenticate")
    public GlobalResponseDto userReject(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.rejectUser(userId, userDetails.getUser());
    }

    // 멤버 관리 페이지 조회
    @GetMapping("kindergarten/{kindergartenId}/user_role/{userRole}")
    public GlobalResponseDto memberPageFind(@PathVariable Long kindergartenId,
                                            @PathVariable UserRoleEnum userRole,
                                            @RequestParam int page,
                                            @RequestParam int size,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.findMemberPage(kindergartenId, userRole, page, size, userDetails.getUser());
    }
}
