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
import com.sparta.finalproject.global.enumType.CurrentStatus;
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

    @Transactional
    public GlobalResponseDto findChildren(Long classroomId, int page) {
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, CHILD_SIZE, Sort.by(direction, "id"));
        Page<Child> children = childRepository.findAllByClassroomId(classroomId, pageable);
        Long childrenCount = childRepository.countByClassroomId(classroomId);
        List<ChildResponseDto> responseDtoList = children.stream().map(ChildResponseDto::of).collect(Collectors.toList());
        return GlobalResponseDto.of(CustomStatusCode.FIND_CHILDREN_SUCCESS, ClassroomChildrenResponseDto.of(childrenCount, responseDtoList));
    }


    // 등하원 시간 설정
    @Transactional
    public GlobalResponseDto modifyAttendanceTime(Long childId, AttendanceModifyRequestDto requestDto) {
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        child.update(requestDto);
        return GlobalResponseDto.of(CustomStatusCode.UPDATE_CHILD_ATTENDANCE_TIME_SUCCESS, AttendanceModifyResponseDto.from(child));
    }

    @Transactional(readOnly = true)
    public GlobalResponseDto findManagerPage(Long classroomId) {
        if (classroomId == 0) {
            Long totalNumber = childRepository.count();
            Long enterNumber = attendanceRepository.countByDateAndEnteredIsTrue(LocalDate.now());
            Long notEnterNumber = attendanceRepository.countByDateAndEnteredIsFalse(LocalDate.now());
            Long exitNumber = attendanceRepository.countByDateAndExitedIsTrue(LocalDate.now());
            ChildrenEnterResponseDto childrenEnterResponseDto = getChildrenEnterListAndCount(classroomId, "전체시간", 0);
            return GlobalResponseDto.of(CustomStatusCode.LOAD_MANAGER_PAGE_SUCCESS,
                    ManagerPageResponseDto.of(totalNumber, enterNumber, notEnterNumber, exitNumber, childrenEnterResponseDto));
        }
        Long totalNumber = childRepository.countByClassroomId(classroomId);
        Long enterNumber = attendanceRepository.countByChild_Classroom_IdAndDateAndEnteredIsTrue(classroomId, LocalDate.now());
        Long notEnterNumber = attendanceRepository.countByChild_Classroom_IdAndDateAndEnteredIsFalse(classroomId, LocalDate.now());
        Long exitNumber = attendanceRepository.countByChild_Classroom_IdAndDateAndExitedIsTrue(classroomId, LocalDate.now());
        ChildrenEnterResponseDto childrenEnterResponseDto = getChildrenEnterListAndCount(classroomId, "전체시간", 0);
        return GlobalResponseDto.of(CustomStatusCode.LOAD_MANAGER_PAGE_SUCCESS,
                ManagerPageResponseDto.of(totalNumber, enterNumber, notEnterNumber, exitNumber, childrenEnterResponseDto));
    }

    // 관리자 페이지 "반 별" 시간대 등/하원 조회
    @Transactional
    public GlobalResponseDto findSchedule(Long classroomId, ScheduleType type, String dailyTimeOfType, int page) {
        if (type.equals(ScheduleType.ENTER)) {
            return GlobalResponseDto.of(CustomStatusCode.FIND_SCHEDULE_SUCCESS, getChildrenEnterListAndCount(classroomId, dailyTimeOfType, page));
        }
        return GlobalResponseDto.of(CustomStatusCode.FIND_SCHEDULE_SUCCESS, getChildrenExitListAndCount(classroomId, dailyTimeOfType, page));
    }

    private ChildrenExitResponseDto getChildrenExitListAndCount(Long classroomId, String dailyExitTime, int page) {
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, CHILD_SIZE, Sort.by(direction, "id"));
        Page<Child> childPage;
        Long childrenCount;
        if (classroomId == 0) {
            if(dailyExitTime.equals("전체시간")){
                childPage = childRepository.findAll(pageable);
                childrenCount = childRepository.count();
            } else {
                childPage = childRepository.findAllByDailyExitTime(dailyExitTime, pageable);
                childrenCount = childRepository.countByDailyExitTime(dailyExitTime);
            }
        } else {
            if(dailyExitTime.equals("전체시간")){
                childPage = childRepository.findAllByClassroomId(classroomId, pageable);
                childrenCount = childRepository.countByClassroomId(classroomId);
            } else {
                childPage = childRepository.findAllByClassroomIdAndDailyExitTime(classroomId, dailyExitTime, pageable);
                childrenCount = childRepository.countByClassroomIdAndDailyExitTime(classroomId, dailyExitTime);
            }
        }
        List<ChildExitResponseDto> childExitResponseDtoList = new ArrayList<>();
        for (Child child : childPage) {
            Attendance childAttendance = attendanceRepository.findByChildAndDate(child, LocalDate.now());
            if (childAttendance.isExited()) {
                childExitResponseDtoList.add(ChildExitResponseDto.of(child, CurrentStatus.EXITED.getCurrentStatus()));
            } else {
                childExitResponseDtoList.add(ChildExitResponseDto.of(child, CurrentStatus.NOT_EXITED.getCurrentStatus()));
            }
        }
        return ChildrenExitResponseDto.of(childrenCount, childExitResponseDtoList);
    }

    private ChildrenEnterResponseDto getChildrenEnterListAndCount(Long classroomId, String dailyEnterTime, int page) {
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, CHILD_SIZE, Sort.by(direction, "id"));
        Page<Child> childPage;
        Long childrenCount;
        if (classroomId == 0) {
            if(dailyEnterTime.equals("전체시간")){
                childPage = childRepository.findAll(pageable);
                childrenCount = childRepository.count();
            } else {
                childPage = childRepository.findAllByDailyEnterTime(dailyEnterTime, pageable);
                childrenCount = childRepository.countByDailyEnterTime(dailyEnterTime);
            }
        } else {
            if(dailyEnterTime.equals("전체시간")){
                childPage = childRepository.findAllByClassroomId(classroomId, pageable);
                childrenCount = childRepository.countByClassroomId(classroomId);
            } else {
                childPage = childRepository.findAllByClassroomIdAndDailyEnterTime(classroomId, dailyEnterTime, pageable);
                childrenCount = childRepository.countByClassroomIdAndDailyEnterTime(classroomId, dailyEnterTime);
            }
        }
        List<ChildEnterResponseDto> childEnterResponseDtoList = new ArrayList<>();
        for (Child child : childPage) {
            Attendance childAttendance = attendanceRepository.findByChildAndDate(child, LocalDate.now());
            if (childAttendance.isEntered()) {
                childEnterResponseDtoList.add(ChildEnterResponseDto.of(child, CurrentStatus.ENTERED.getCurrentStatus()));
            } else {
                childEnterResponseDtoList.add(ChildEnterResponseDto.of(child, CurrentStatus.NOT_ENTERED.getCurrentStatus()));
            }
        }
        return ChildrenEnterResponseDto.of(childrenCount, childEnterResponseDtoList);
    }
}

