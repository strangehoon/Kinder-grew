package com.sparta.finalproject.domain.child.service;


import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.dto.*;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.entity.ScheduleType;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final ClassroomRepository classroomRepository;
    private final AttendanceRepository attendanceRepository;
    private final S3Service s3Service;
    private static LocalTime startTime;
    private static LocalTime endTime;
    private static final int CHILD_SIZE = 15;

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

    @Transactional(readOnly = true)
    public GlobalResponseDto findManagerPage(Long classroomId) {
        Long totalNumber = childRepository.count();
        Long enterNumber = attendanceRepository.countByChild_Classroom_IdAndDateAndEnteredIsTrue(classroomId, LocalDate.now());
        Long notEnterNumber = attendanceRepository.countByChild_Classroom_IdAndDateAndEnteredIsFalse(classroomId, LocalDate.now());
        Long exitNumber = attendanceRepository.countByChild_Classroom_IdAndDateAndExitedIsTrue(classroomId, LocalDate.now());
        List<ChildEnterResponseDto> childEnterResponseDtoList = getChildEnterResponseDtoList(classroomId, 1, 0);
        return GlobalResponseDto.of(CustomStatusCode.LOAD_MANAGER_PAGE_SUCCESS,
                ManagerPageResponseDto.of(totalNumber,enterNumber,notEnterNumber,exitNumber,childEnterResponseDtoList));
    }

    // 관리자 페이지 "반 별" 시간대 등/하원 조회
    @Transactional
    public GlobalResponseDto findSchedule(Long classroomId, ScheduleType type, int time, int page) {
        if (type.equals(ScheduleType.ENTER)) {
            return GlobalResponseDto.of(CustomStatusCode.FIND_SCHEDULE_SUCCESS, getChildEnterResponseDtoList(classroomId, time, page));
        }
        return GlobalResponseDto.of(CustomStatusCode.FIND_SCHEDULE_SUCCESS, getChildExitResponseDtoList(classroomId, time, page));
    }

    private List<ChildExitResponseDto> getChildExitResponseDtoList(Long classroomId, int time, int page) {
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, CHILD_SIZE, Sort.by(direction, "dailyExitTime"));
        setStartTime(ScheduleType.EXIT, time);
        Page<Child> childPage = getChildByPage(classroomId, startTime, endTime, pageable);
        List<ChildExitResponseDto> ChildExitResponseDtoList = new ArrayList<>();
        for (Child child : childPage) {
            Attendance childAttendance = attendanceRepository.findByChildAndDate(child, LocalDate.now());
            ChildExitResponseDtoList.add(ChildExitResponseDto.of(child, childAttendance));
        }
        return ChildExitResponseDtoList;
    }

    private List<ChildEnterResponseDto> getChildEnterResponseDtoList(Long classroomId, int time, int page) {
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, CHILD_SIZE, Sort.by(direction, "dailyEnterTime"));
        setStartTime(ScheduleType.ENTER, time);
        Page<Child> childPage = getChildByPage(classroomId, startTime, endTime, pageable);
        List<ChildEnterResponseDto> childEnterResponseDtoList = new ArrayList<>();
        for (Child child : childPage) {
            Attendance childAttendance = attendanceRepository.findByChildAndDate(child, LocalDate.now());
            childEnterResponseDtoList.add(ChildEnterResponseDto.of(child, childAttendance));
        }
        return childEnterResponseDtoList;
    }


    private Page<Child> getChildByPage(Long classroomId, LocalTime startTime, LocalTime endTime, Pageable pageable) {
        if (classroomId > 0) {
            return childRepository.findAllByClassroomIdAndDailyEnterTimeBetween(classroomId, startTime, endTime, pageable);
        }
        return childRepository.findAllByDailyEnterTimeBetween(startTime, endTime, pageable);
    }

    private void setStartTime(ScheduleType type, int time) {
        if (type.equals(ScheduleType.ENTER)) {
            if (time == 1) {
                startTime = LocalTime.of(0, 0, 1);
                endTime = LocalTime.of(23, 59, 59);
            } else if (time == 2) {
                startTime = LocalTime.of(7, 0, 0);
                endTime = LocalTime.of(8, 0, 0);
            } else if (time == 3) {
                startTime = LocalTime.of(8, 0, 0);
                endTime = LocalTime.of(9, 0, 0);
            } else if (time == 4) {
                startTime = LocalTime.of(9, 0, 0);
                endTime = LocalTime.of(10, 0, 0);
            } else {
                throw new IllegalArgumentException("숫자 잘못 전달");
            }
        } else {
            if (time == 1) {
                startTime = LocalTime.of(0, 0, 1);
                endTime = LocalTime.of(23, 59, 59);
            } else if (time == 2) {
                startTime = LocalTime.of(16, 0, 0);
                endTime = LocalTime.of(17, 0, 0);
            } else if (time == 3) {
                startTime = LocalTime.of(17, 0, 0);
                endTime = LocalTime.of(18, 0, 0);
            } else if (time == 4) {
                startTime = LocalTime.of(18, 0, 0);
                endTime = LocalTime.of(19, 0, 0);
            } else {
                throw new IllegalArgumentException("숫자 잘못 전달");
            }
        }
    }
}
