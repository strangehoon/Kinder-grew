package com.sparta.finalproject.domain.kindergarten.service;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.kindergarten.dto.KindergartenRequestDto;
import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
import com.sparta.finalproject.domain.kindergarten.repository.KindergartenRepository;
import com.sparta.finalproject.domain.user.dto.PrincipalModifyResponseDto;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.domain.user.repository.UserRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KindergartenService {

    private final KindergartenRepository kindergartenRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Transactional
    public GlobalResponseDto addKindergarten(KindergartenRequestDto requestDto, User user) {
        if(!user.getRole().equals(UserRoleEnum.PRINCIPAL)){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        Kindergarten kindergarten = kindergartenRepository.save(Kindergarten.from(requestDto));
        for (String classroomName : requestDto.getClassroomList()){
            classroomRepository.save(Classroom.of(classroomName, kindergarten));
        }
        user.setKindergarten(kindergarten);
        userRepository.save(user);
        return GlobalResponseDto.of(CustomStatusCode.FINAL_SIGNUP_PRINCIPAL, PrincipalModifyResponseDto.from(user));
    }
}
