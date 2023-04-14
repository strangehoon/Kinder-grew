package com.sparta.finalproject.domain.child.service;


import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.dto.*;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.user.dto.ParentProfileResponseDto;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.domain.user.repository.UserRepository;
import com.sparta.finalproject.domain.user.service.UserService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.CommuteStatus;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import com.sparta.finalproject.global.response.exceptionType.UserException;
import com.sparta.finalproject.global.validator.UserValidator;
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.finalproject.global.enumType.AttendanceStatus.*;
import static com.sparta.finalproject.global.enumType.CommuteStatus.ENTER;
import static com.sparta.finalproject.global.enumType.UserRoleEnum.PRINCIPAL;
import static com.sparta.finalproject.global.enumType.UserRoleEnum.TEACHER;
import static com.sparta.finalproject.global.response.CustomStatusCode.LOAD_MANAGER_PAGE_SUCCESS;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChildService {

    private final ChildRepository childRepository;
    private final ClassroomRepository classroomRepository;
    private final AttendanceRepository attendanceRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final UserService userService;
    private static final int CHILD_SIZE = 14;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

    //반별 아이 생성
    @Transactional
    public GlobalResponseDto addChild(Long classroomId, ChildRequestDto childRequestDto, User user) throws IOException {
        UserValidator.validateTeacherAndPrincipal(user);
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));

        String profileImageUrl = "https://hanghaefinals3.s3.ap-northeast-2.amazonaws.com/profile-image/default_profile_image.jpeg";

        if(childRequestDto.getImage() != null) {

            profileImageUrl = s3Service.upload(childRequestDto.getImage(), "profile-image");
        }

        Child child;
        User parent;
        if (childRequestDto.getParentId() != null) {
            parent = userRepository.findById(childRequestDto.getParentId()).orElseThrow(
                    () -> new UserException(CustomStatusCode.USER_NOT_FOUND));
            child = childRepository.save(Child.of(childRequestDto, classroom, profileImageUrl, parent));
        } else {
            child = childRepository.save(Child.of(childRequestDto, classroom, profileImageUrl));
        }
        attendanceRepository.save(Attendance.of(child, 미등원));
        return GlobalResponseDto.of(CustomStatusCode.ADD_CHILD_SUCCESS, ChildResponseDto.of(child));
    }

    //반별 아이 프로필 선택 조회
    @Transactional
    public GlobalResponseDto findChild(Long classroomId, Long childId, User user) {
        UserValidator.validateParentAndTeacherAndPrincipal(user);
        Child child = childRepository.findByClassroomIdAndId(classroomId, childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));

        User parent = child.getUser();
        if (parent != null) {
            return GlobalResponseDto.of(CustomStatusCode.FIND_CHILD_SUCCESS, ChildResponseDto.of(child, ParentProfileResponseDto.from(parent)));
        }
        return GlobalResponseDto.of(CustomStatusCode.FIND_CHILD_SUCCESS, ChildResponseDto.of(child));
    }

    //반별 아이 프로필 수정
    @Transactional
    public GlobalResponseDto modifyChild(Long classroomId, Long childId, ChildRequestDto childRequestDto, User user) throws IOException {

        UserValidator.validateTeacherAndPrincipal(user);

        Child child = childRepository.findByClassroomIdAndId(classroomId, childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CustomStatusCode.CLASSROOM_NOT_FOUND));

        String profileImageUrl = userService.getProfileImageUrl(childRequestDto, user);

        if (childRequestDto.getParentId() != null) {
            User parent = userRepository.findById(childRequestDto.getParentId()).orElseThrow(
                    () -> new UserException(CustomStatusCode.USER_NOT_FOUND));
            child.update(childRequestDto, classroom, profileImageUrl, parent);
        }
        return GlobalResponseDto.of(CustomStatusCode.MODIFY_CHILD_SUCCESS, ChildResponseDto.of(child));
    }

    //반별 아이 한명 검색
    @Transactional(readOnly = true)
    public GlobalResponseDto findChildByName(Long classroomId, String name, User user) {
        UserValidator.validateParentAndTeacherAndPrincipal(user);
        Long childrenCount = childRepository.countAllByClassroomId(classroomId);

        List<Child> childList = childRepository.findByClassroomIdAndNameContaining(classroomId, name);
        List<ChildResponseDto> childResponseDtoList = new ArrayList<>();
        for(Child child1 : childList) {
            childResponseDtoList.add(ChildResponseDto.of(child1));
        }
        return GlobalResponseDto.of(CustomStatusCode.SEARCH_CHILD_SUCCESS, ClassroomChildrenResponseDto.of(childrenCount, childResponseDtoList));
    }

    @Transactional
    public GlobalResponseDto findChildren(Long classroomId, int page, User user) {
        UserValidator.validateParentAndTeacherAndPrincipal(user);
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, CHILD_SIZE, Sort.by(direction, "id"));
        Page<Child> children = childRepository.findAllByClassroomId(classroomId, pageable);
        Long childrenCount = childRepository.countAllByClassroomId(classroomId);
        List<ChildResponseDto> responseDtoList = children.stream().map(ChildResponseDto::of).collect(Collectors.toList());
        return GlobalResponseDto.of(CustomStatusCode.FIND_CHILDREN_SUCCESS, ClassroomChildrenResponseDto.of(childrenCount, responseDtoList));
    }


    // 등하원 시간 설정
    @Transactional
    public GlobalResponseDto modifyAttendanceTime(Long childId, AttendanceModifyRequestDto requestDto, User user) {
        UserValidator.validateParent(user);
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        child.update(requestDto);
        return GlobalResponseDto.of(CustomStatusCode.UPDATE_CHILD_ATTENDANCE_TIME_SUCCESS, AttendanceModifyResponseDto.from(child));
    }

    // 등하원 시간 조회(학부모)
    @Transactional(readOnly = true)
    public GlobalResponseDto findAttendanceTime(Long childId, User user){
        UserValidator.validateParent(user);
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        return GlobalResponseDto.of(CustomStatusCode.GET_CHILD_ATTENDANCE_TIME, ChildAttendanceTimeResponseDto.from(child));
    }

    // 관리자 페이지 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findChildSchedule(int page, int size, Long classroomId, String state, String time, User user) {

        UserValidator.validateTeacherAndPrincipal(user);

        CommuteStatus commuteStatus = CommuteStatus.valueOf(state);

        if(time.equals("전체시간")) {
            time = null;
        }
        if(classroomId == 0) {
            classroomId = null;
        }

        List<Child> childListAll = childRepository.findAllByClassroomId(classroomId);
        List<Child> childListEntered = childRepository.findAllByEnteredAndClassroomId(LocalDate.now(), 등원, classroomId);
        List<Child> childListNotEntered = childRepository.findAllByEnteredAndClassroomId(LocalDate.now(), 미등원, classroomId);
        List<Child> childListExited = childRepository.findAllByEnteredAndClassroomId(LocalDate.now(), 하원, classroomId);
        List<Child> childListAbsented = childRepository.findAllByEnteredAndClassroomId(LocalDate.now(), 결석, classroomId);

        InfoDto infoDto = InfoDto.of(childListAll.size(), childListEntered.size(), childListNotEntered.size(),
                childListExited.size(), childListAbsented.size());
        Pageable pageable = PageRequest.of(page, size);

        Page<ChildScheduleResponseDto> plist = childRepository.findChildSchedule(classroomId, commuteStatus, time, pageable, infoDto);
        List<ChildScheduleResponseDto> list = plist.getContent();
        for (ChildScheduleResponseDto responseDto : list) {
            LocalTime enterTime = responseDto.getEnterTime();
            LocalTime exitTime = responseDto.getExitTime();
            if ((enterTime == null) && (exitTime == null))
                responseDto.update(미등원);
            else if ((enterTime != null) && (exitTime == null)) {
                if (commuteStatus == ENTER)
                    responseDto.update(등원);
                else
                    responseDto.update(미하원);
            } else
                responseDto.update(하원);
        }
        return GlobalResponseDto.of(LOAD_MANAGER_PAGE_SUCCESS, plist);
    }





    //학부모 페이지 아이 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findParentPageChild(Long childId, User user) {
        UserValidator.validateParent(user);
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));

        return GlobalResponseDto.of(CustomStatusCode.FIND_CHILD_SUCCESS, ChildProfileResponseDto.from(child));
    }

    //학부모 페이지 아이 수정
    @Transactional
    public GlobalResponseDto modifyParentPageChild(Long childId, ChildRequestDto childRequestDto, User user) throws IOException {
        UserValidator.validateParent(user);

        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));

        String profileImageUrl = userService.getProfileImageUrl(childRequestDto, user);

        child.update(childRequestDto,profileImageUrl);
        return GlobalResponseDto.of(CustomStatusCode.FIND_CHILD_SUCCESS, ChildProfileResponseDto.from(child));
    }
}