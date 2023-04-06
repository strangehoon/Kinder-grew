package com.sparta.finalproject.domain.child.service;


import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.dto.*;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.State;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.finalproject.global.enumType.Status.*;
import static com.sparta.finalproject.global.enumType.State.ENTER;
import static com.sparta.finalproject.global.response.CustomStatusCode.LOAD_MANAGER_PAGE_SUCCESS;

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

    // 관리자 페이지 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findChildSchedule(int page, int size, Long classroomId, String state, String time) {

        ChildScheduleRequestDto requestDto = new ChildScheduleRequestDto();
        requestDto.setTime(time);
        requestDto.setState(State.valueOf(state));
        if(classroomId==0){
            requestDto.setClassroomId(null);
        }
        else
            requestDto.setClassroomId(classroomId);
        if(classroomId==null) {
            List<Child> childListAll = childRepository.findAll();
            List<Child> childListEntered = childRepository.findAllByEntered(LocalDate.now(), 등원);
            List<Child> childListNotEntered = childRepository.findAllByEntered(LocalDate.now(), 미등원);
            List<Child> childListExited = childRepository.findAllByEntered(LocalDate.now(), 하원);
            InfoDto info = new InfoDto(childListAll.size(), childListEntered.size(), childListNotEntered.size(), childListExited.size());
            Pageable pageable = PageRequest.of(page, size);
            Page<ChildScheduleResponseDto> plist = childRepository.findChildSchedule(requestDto, pageable, info);
            List<ChildScheduleResponseDto> list = plist.getContent();

            for(ChildScheduleResponseDto responseDto : list){
                LocalTime enterTime = responseDto.getEnterTime();
                LocalTime exitTime = responseDto.getExitTime();
                if((enterTime==null)&&(exitTime== null))
                    responseDto.update(미등원);
                else if ((enterTime!=null)&&(exitTime==null)) {
                    if(requestDto.getState()==ENTER)
                        responseDto.update(등원);
                    else
                        responseDto.update(미하원);
                }
                else
                    responseDto.update(하원);
            }
            return GlobalResponseDto.of(LOAD_MANAGER_PAGE_SUCCESS, plist);
        }
        else {
            List<Child> childListAll = childRepository.findAllByClassroomId(requestDto.getClassroomId());
            List<Child> childListEntered = childRepository.findAllByEnteredAndClassroom(LocalDate.now(), 등원, requestDto.getClassroomId());
            List<Child> childListNotEntered = childRepository.findAllByEnteredAndClassroom(LocalDate.now(), 미등원, requestDto.getClassroomId());
            List<Child> childListExited = childRepository.findAllByEnteredAndClassroom(LocalDate.now(), 하원, requestDto.getClassroomId());
            InfoDto info = new InfoDto(childListAll.size(), childListEntered.size(), childListNotEntered.size(), childListExited.size());
            Pageable pageable = PageRequest.of(page, size);
            Page<ChildScheduleResponseDto> plist = childRepository.findChildSchedule(requestDto, pageable, info);
            List<ChildScheduleResponseDto> list = plist.getContent();

            for(ChildScheduleResponseDto responseDto : list){
                LocalTime enterTime = responseDto.getEnterTime();
                LocalTime exitTime = responseDto.getExitTime();

                if((enterTime==null)&&(exitTime== null))
                    responseDto.update(미등원);
                else if ((enterTime!=null)&&(exitTime==null)) {
                    if(requestDto.getState()==ENTER)
                        responseDto.update(등원);
                    else
                        responseDto.update(미하원);
                }
                else
                    responseDto.update(하원);
            }
            return GlobalResponseDto.of(LOAD_MANAGER_PAGE_SUCCESS, plist);
        }
    }
}

