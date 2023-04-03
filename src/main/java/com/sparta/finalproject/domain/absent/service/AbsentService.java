package com.sparta.finalproject.domain.absent.service;

import com.sparta.finalproject.domain.absent.dto.AbsentCancelRequestDto;
import com.sparta.finalproject.domain.absent.dto.AbsentAddRequestDto;
import com.sparta.finalproject.domain.absent.dto.AbsentAddResponseDto;
import com.sparta.finalproject.domain.absent.entity.AbsentInfo;
import com.sparta.finalproject.domain.absent.repository.AbsentInfoRepository;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsentService {

    private final AbsentInfoRepository absentInfoRepository;

    private final ChildRepository childRepository;

    // 결석 신청
    @Transactional
    public GlobalResponseDto addAbsent(Long childId, AbsentAddRequestDto requestDto) {
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        AbsentInfo absentInfo = AbsentInfo.of(requestDto, child);
        absentInfoRepository.save(absentInfo);
        return GlobalResponseDto.of(CustomStatusCode.CREATE_ABSENT_SUCCESS, AbsentAddResponseDto.from(absentInfo));
    }

    //결석 취소
    @Transactional
    public GlobalResponseDto removeAbsent(Long childId, AbsentCancelRequestDto requestDto) {
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );

        List<String> absentPeriodList = requestDto.getAbsentList();
        for (String absentPeriod : absentPeriodList) {
            String []str = absentPeriod.split("   ");
            LocalDate startDate = LocalDate.parse(str[0], DateTimeFormatter.ISO_DATE);
            LocalDate endDate = LocalDate.parse(str[1], DateTimeFormatter.ISO_DATE);
            absentInfoRepository.deleteByStartDateAndEndDate(startDate, endDate);
        }
        return GlobalResponseDto.from(CustomStatusCode.DELETE_ABSENT_SUCCESS);
    }

}
