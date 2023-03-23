package com.sparta.finalproject.domain.parent.service;


import com.sparta.finalproject.domain.absent.entity.AbsentInfo;
import com.sparta.finalproject.domain.absent.repository.AbsentInfoRepository;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.parent.dto.ParentPageResponseDto;
import com.sparta.finalproject.domain.parent.dto.ParentProfileModifyRequestDto;
import com.sparta.finalproject.domain.parent.dto.ParentProfileModifyResponseDto;
import com.sparta.finalproject.domain.parent.entity.Parent;
import com.sparta.finalproject.domain.parent.repository.ParentRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import com.sparta.finalproject.global.response.exceptionType.GlobalException;
import com.sparta.finalproject.global.response.exceptionType.ParentException;
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;

    private final ChildRepository childRepository;

    private final AbsentInfoRepository absentInfoRepository;

    private final S3Service s3Service;

    // 학부모 페이지 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findParentProfile(Long parentId, Long childId) {
        Parent parent = parentRepository.findById(parentId).orElseThrow(
                () -> new ParentException(CustomStatusCode.PARENT_NOT_FOUND)
        );
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );

        YearMonth today = YearMonth.now();
        LocalDate startDate = today.atDay(1);
        LocalDate endDate = today.atEndOfMonth();

        List<String> absentPeriodList = new ArrayList<>();
        List<AbsentInfo> absentInfoList = absentInfoRepository.findAllByChildAndStartDateBetween(child, startDate, endDate);
        for (AbsentInfo absentInfo : absentInfoList) {
            String period = absentInfo.getStartDate() + "   " + absentInfo.getEndDate();
            absentPeriodList.add(period);
        }
        return GlobalResponseDto.of(CustomStatusCode.GET_PARENT_SUCCESS, ParentPageResponseDto.from(parent, child, absentPeriodList));
    }

    // 학부모 프로필 수정
    @Transactional
    public GlobalResponseDto modifyProfile(ParentProfileModifyRequestDto requestDto, Long parentId, Long childId) throws IOException {
        Parent parent = parentRepository.findById(parentId).orElseThrow(
                () -> new IllegalArgumentException()
        );
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new IllegalArgumentException()
        );
        String parentImageUrl = s3Service.upload(requestDto.getParentImage(), "profile-image");
        String childImageUrl = s3Service.upload(requestDto.getChildImage(), "profile-image");

        parent.update(requestDto, parentImageUrl);
        child.update(requestDto, childImageUrl);
        return GlobalResponseDto.of(CustomStatusCode.UPDATE_PARENT_SUCCESS, ParentProfileModifyResponseDto.from(parent, child));
    }


}
