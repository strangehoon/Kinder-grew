package com.sparta.finalproject.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.domain.user.dto.ParentSignupRequestDto;
import com.sparta.finalproject.domain.user.dto.TeacherSignupRequestDto;
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
    public GlobalResponseDto parentModify(@Valid @ModelAttribute ParentSignupRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        log.info(requestDto.getName());

        return userService.modifyParent(requestDto, userDetails.getUser());
    }


    @PutMapping("/teacher/info")
    public GlobalResponseDto teacherModify(@Valid @ModelAttribute TeacherSignupRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{

        log.info(requestDto.getName());

        return userService.modifyTeacher(requestDto, userDetails.getUser());
    }
}
