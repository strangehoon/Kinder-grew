package com.sparta.finalproject.domain.kindergarten.service;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.kindergarten.dto.KindergartenRequestDto;
import com.sparta.finalproject.domain.kindergarten.dto.KindergartenResponseDto;
import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
import com.sparta.finalproject.domain.kindergarten.repository.KindergartenRepository;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.domain.user.repository.UserRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.KindergartenException;
import com.sparta.finalproject.global.validator.UserValidator;
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KindergartenService {

    private final KindergartenRepository kindergartenRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public GlobalResponseDto addKindergarten(KindergartenRequestDto requestDto, User user) throws IOException {
        UserValidator.validatePrincipal(user);

        String logoImageUrl;

        if (requestDto.isCancelled()) {
            logoImageUrl = "https://hanghaefinals3.s3.ap-northeast-2.amazonaws.com/logo-image/default_logo_image.png";
        } else {
            logoImageUrl = "https://hanghaefinals3.s3.ap-northeast-2.amazonaws.com/logo-image/default_logo_image.png";

            if (requestDto.getLogoImage() != null) {
                logoImageUrl = s3Service.upload(requestDto.getLogoImage(), "logo-image");
            }
        }

        Kindergarten kindergarten = kindergartenRepository.save(Kindergarten.of(requestDto, logoImageUrl));
        for (String classroomName : requestDto.getClassroomList()){
            classroomRepository.save(Classroom.of(classroomName, kindergarten));
        }
        user.setKindergarten(kindergarten);
        userRepository.save(user);
        return GlobalResponseDto.of(CustomStatusCode.SIGN_UP_SUCCESS, null);
    }

    @Transactional
    public GlobalResponseDto findKindergarten(String keyword, User user) {
        UserValidator.validateEarlyUser(user);
        List<Kindergarten> kindergartenList = kindergartenRepository.findAllByKindergartenNameContaining(keyword);
        List<KindergartenResponseDto> responseDtoList = kindergartenList.stream().map(KindergartenResponseDto::from).collect(Collectors.toList());
        return GlobalResponseDto.of(CustomStatusCode.SEARCH_KINDERGARTEN_SUCCESS, responseDtoList);
    }

    @Transactional
    public GlobalResponseDto selectKindergarten(Long kindergartenId, User user){
        UserValidator.validateEarlyUser(user);
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(CustomStatusCode.KINDERGARTEN_NOT_FOUND)
        );
        user.setKindergarten(kindergarten);
        userRepository.save(user);
        return GlobalResponseDto.of(CustomStatusCode.SELECT_KINDERGARTEN_SUCCESS, null);
    }
}
