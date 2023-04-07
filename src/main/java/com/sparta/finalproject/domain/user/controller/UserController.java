package com.sparta.finalproject.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.domain.user.dto.ParentModifyRequestDto;
import com.sparta.finalproject.domain.user.dto.TeacherModifyRequestDto;
import com.sparta.finalproject.domain.user.dto.TeacherProfileModifyRequestDto;
import com.sparta.finalproject.domain.user.service.UserService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Authorization")
    @GetMapping("/oauth/kakao/callback")
    public GlobalResponseDto userLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        return userService.loginUser(code, response);
    }

    @PutMapping("/parent/info")
    public GlobalResponseDto parentModify(@Valid @ModelAttribute ParentModifyRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.modifyParent(requestDto, userDetails.getUser());
    }


    @PutMapping("/teacher/info")
    public GlobalResponseDto teacherModify(@Valid @ModelAttribute TeacherModifyRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.modifyTeacher(requestDto, userDetails.getUser());
    }

    @GetMapping("/user/profile")
    public GlobalResponseDto userProfileDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.detailsUserProfile(userDetails.getUser());
    }

    @PutMapping("/parent/profile")
    public GlobalResponseDto parentProfileModify(@Valid @ModelAttribute ParentModifyRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.modifyParentProfile(requestDto, userDetails.getUser());
    }

    @PutMapping("/teacher/profile")
    public GlobalResponseDto teacherProfileModify(@Valid @ModelAttribute TeacherProfileModifyRequestDto requestDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return userService.modifyTeacherProfile(requestDto, userDetails.getUser());
    }

    @GetMapping("/teacher")
    public GlobalResponseDto teacherListFind(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.findTeacherList(userDetails.getUser());
    }
}
