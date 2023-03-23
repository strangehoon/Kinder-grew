package com.sparta.finalproject.domain.absent.service;

import com.sparta.finalproject.domain.absent.dto.AbsentPostRequestDto;
import com.sparta.finalproject.domain.absent.dto.AbsentPostResponseDto;
import com.sparta.finalproject.domain.absent.entity.AbsentInfo;
import com.sparta.finalproject.domain.absent.repository.AbsentInfoRepository;
import com.sparta.finalproject.domain.child.dto.AttendanceModifyResponseDto;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.AbsentException;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AbsentService {

    private final AbsentInfoRepository absentInfoRepository;

    private final ChildRepository childRepository;

    // 결석 신청
    @Transactional
    public GlobalResponseDto addAbsent(Long childId, AbsentPostRequestDto requestDto) {
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        AbsentInfo absentInfo = AbsentInfo.of(requestDto, child);
        absentInfoRepository.save(absentInfo);
        return GlobalResponseDto.of(CustomStatusCode.CREATE_ABSENT_SUCCESS, AbsentPostResponseDto.from(absentInfo));
    }
}
