package com.sparta.finalproject.domain.child.service;


import com.sparta.finalproject.domain.child.dto.AttendanceModifyRequestDto;
import com.sparta.finalproject.domain.child.dto.AttendanceModifyResponseDto;
import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.entity.ScheduleType;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.dto.ClassroomResponseDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final ClassroomRepository classroomRepository;
    private final S3Service s3Service;

    //반별 아이 생성
    @Transactional
    public GlobalResponseDto addChild(Long classroomId, ChildRequestDto childRequestDto) throws IOException {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));
        String profileImageUrl = s3Service.upload(childRequestDto.getImage(), "profile-image");
        Child child = childRepository.save(Child.of(childRequestDto, classroom, profileImageUrl));
        return GlobalResponseDto.of(CustomStatusCode.ADD_CHILD_SUCCESS, ChildResponseDto.of(child));
    }

    //반별 아이 프로필 선택 조회
    @Transactional
    public GlobalResponseDto findChild(Long classroomId, Long childId) {
        Child child = childRepository.findByClassroomIdAndId(classroomId, childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));
        return GlobalResponseDto.of(CustomStatusCode.FIND_CHILD_SUCCESS, ChildResponseDto.of(child));
    }

    //반별 아이 프로필 수정
    @Transactional
    public GlobalResponseDto modifyChild(Long classroomId, Long childId, ChildRequestDto childRequestDto) throws IOException {
        Child child = childRepository.findByClassroomIdAndId(classroomId, childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CustomStatusCode.CLASSROOM_NOT_FOUND)
        );
        String profileImageUrl = s3Service.upload(childRequestDto.getImage(), "profile-image");
        child.update(childRequestDto, classroom, profileImageUrl);
        return GlobalResponseDto.of(CustomStatusCode.MODIFY_CHILD_SUCCESS, ChildResponseDto.of(child));
    }

    //반별 아이 한명 검색
    @Transactional(readOnly = true)
    public GlobalResponseDto findChildByName(Long classroomId, String name) {
        Child child = childRepository.findByClassroomIdAndName(classroomId, name).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        return GlobalResponseDto.of(CustomStatusCode.SEARCH_CHILD_SUCCESS, ChildResponseDto.of(child));
    }

    public GlobalResponseDto findChildren(Long classroomId) {
        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<ChildResponseDto> responseDtoList = children.stream().map(ChildResponseDto::of).collect(Collectors.toList());
        return GlobalResponseDto.of(CustomStatusCode.FIND_CHILDREN_SUCCESS, responseDtoList);
    }

    // 등하원 시간 설정
    @Transactional
    public GlobalResponseDto updateAttendanceTime(Long childId, AttendanceModifyRequestDto requestDto) {
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        child.update(requestDto);
        return GlobalResponseDto.of(CustomStatusCode.UPDATE_CHILD_ATTENDANCE_TIME_SUCCESS, AttendanceModifyResponseDto.from(child));
    }

    // 관리자 페이지 "전체" 시간대 등,하원 조회
    public GlobalResponseDto scheduleManager(ScheduleType type, int time, int page, int size) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (type == ScheduleType.EXIT) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "time"));

        LocalTime dailyEnterTime;
        LocalTime dailyExitDate;
        if (time == 1) {
            // 전체 시간대
            dailyEnterTime = LocalTime.MIN;
            dailyExitDate = LocalTime.MAX;
        } else if (time == 2) {
            // 7:00~8:00
            dailyEnterTime = LocalTime.of(7, 0, 0);
            dailyExitDate = LocalTime.of(8, 0, 0);
        } else if (time == 3) {
            // 8:00~9:00
            dailyEnterTime = LocalTime.of(8, 0, 0);
            dailyExitDate = LocalTime.of(9, 0, 0);
        } else {
            // 9:00~10:00
            dailyEnterTime = LocalTime.of(9, 0, 0);
            dailyExitDate = LocalTime.of(10, 0, 0);
        }

        Page<Child> childPage = childRepository.findAllByDailyEnterTimeBetween(dailyEnterTime, dailyExitDate, pageable);
        List<ChildResponseDto> childResponseDto = new ArrayList<>();
        for (Child child : childPage) {
            childResponseDto.add(ChildResponseDto.of(child));
        }
        return GlobalResponseDto.of(CustomStatusCode.MODIFY_CHILD_SUCCESS, ClassroomResponseDto.of(childResponseDto));
    }

    // 관리자 페이지 "반 별" 시간대 등/하원 조회
    public GlobalResponseDto classroomScheduleManager(Long classroomId, ScheduleType type, int time, int page, int size) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (type == ScheduleType.EXIT) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "time"));

        LocalTime dailyEnterTime;
        LocalTime dailyExitDate;
        if (time == 1) {
            // 전체 시간대
            dailyEnterTime = LocalTime.MIN;
            dailyExitDate = LocalTime.MAX;
        } else if (time == 2) {
            // 7:00~8:00
            dailyEnterTime = LocalTime.of(7, 0, 0);
            dailyExitDate = LocalTime.of(8, 0, 0);
        } else if (time == 3) {
            // 8:00~9:00
            dailyEnterTime = LocalTime.of(8, 0, 0);
            dailyExitDate = LocalTime.of(9, 0, 0);
        } else {
            // 9:00~10:00
            dailyEnterTime = LocalTime.of(9, 0, 0);
            dailyExitDate = LocalTime.of(10, 0, 0);
        }

        Page<Child> childPage = childRepository.findAllByClassroomIdAndDailyEnterTimeBetween(classroomId, dailyEnterTime, dailyExitDate, pageable);
        List<ChildResponseDto> childResponseDto = new ArrayList<>();
        for (Child child : childPage) {
            childResponseDto.add(ChildResponseDto.of(child));
        }
        return GlobalResponseDto.of(CustomStatusCode.MODIFY_CHILD_SUCCESS, ClassroomResponseDto.of(childResponseDto));
    }
}
