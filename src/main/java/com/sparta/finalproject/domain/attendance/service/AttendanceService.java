package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.dto.*;
import com.sparta.finalproject.domain.attendance.entity.AbsentInfo;
import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.attendance.repository.AbsentInfoRepository;
import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.dto.ClassroomInfoDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
import com.sparta.finalproject.domain.kindergarten.repository.KindergartenRepository;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.Day;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.AttendanceException;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import com.sparta.finalproject.global.response.exceptionType.KindergartenException;
import com.sparta.finalproject.global.validator.UserValidator;
import com.sparta.finalproject.infra.kakaoMessage.CustomMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.finalproject.global.enumType.AttendanceStatus.*;
import static com.sparta.finalproject.global.enumType.Day.*;
import static com.sparta.finalproject.global.response.CustomStatusCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final ChildRepository childRepository;
    private final AttendanceRepository attendanceRepository;
    private final AbsentInfoRepository absentInfoRepository;

    private final CustomMessageService messageService;

    private final ClassroomRepository classroomRepository;
    private final KindergartenRepository kindergartenRepository;

    // 출결 테이블 자동 생성
    @Transactional
    @Scheduled(cron = "0 0 0 * * 1-6", zone = "Asia/Seoul")
    public void addDailyAttendance(){
        List<Child> children = childRepository.findAll();
        List<Attendance> attendanceList = new ArrayList<>();
        if(children.isEmpty())
            throw new ChildException(CHILD_NOT_FOUND);
        for(Child child : children){
            if(attendanceRepository.findByChildAndDate(child, LocalDate.now()).isEmpty())
                attendanceList.add(Attendance.of(child, 미등원, LocalDate.now()));
        }
        attendanceRepository.saveAll(attendanceList);
    }

    // 23시 59분에 미등원이면 결석처리, 나머지는 출석으로 처리
    @Transactional
    @Scheduled(cron = "0 59 23 * * 1-6", zone = "Asia/Seoul")
    public void handleDailyAttendance(){
        List<Child> children = childRepository.findAll();
        if(children.isEmpty())
            throw new ChildException(CHILD_NOT_FOUND);
        for(Child child : children){
            Attendance attendance = attendanceRepository.findByChildIdAndDate(child.getId(), LocalDate.now()).orElseThrow(
                    () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
            );
            if(attendance.getStatus()==미등원){
                attendance.update(결석, null, null, "무단결석");
            }
            else if(attendance.getStatus()==하원){
                attendance.update(출석);
            }
            else{
                attendance.update(출석, LocalTime.parse("20:00:00"));
            }
        }
    }

    @Transactional
    public GlobalResponseDto modifyEnterStatus(Long childId, User user){
        UserValidator.validateTeacherAndPrincipal(user);

        Attendance attendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now()).orElseThrow(
                () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
        );
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CHILD_NOT_FOUND)
        );
        String accessToken = user.getAccessToken();
        String kindergartenName = child.getClassroom().getKindergarten().getKindergartenName();
        String childName = child.getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String enterTime;
        String exitTime;
        Long kakaoId = child.getUser().getKakaoId();

        if(attendance.getEnterTime() == null){
            attendance.enter(LocalTime.now(), 등원);
            enterTime = attendance.getEnterTime().format(formatter);
            exitTime = null;
            Boolean flag = messageService.sendToFriendMessage(등원,accessToken, kindergartenName, childName, enterTime, exitTime, kakaoId);
            if(flag == false){
                return GlobalResponseDto.from(MESSAGE_NOT_TRANSPORT);
            }
            else
                return GlobalResponseDto.from(CustomStatusCode.CHILD_ENTER_SUCCESS);
        }

        else {
            attendance.enter(null, 미등원);
            Boolean flag = messageService.sendToFriendMessage(등원, accessToken, kindergartenName, childName, kakaoId);
            if(flag == false){
                return GlobalResponseDto.from(MESSAGE_NOT_TRANSPORT);
            }
            else
                return GlobalResponseDto.from(CHILD_ENTER_CANCEL);
        }
    }

    @Transactional
    public GlobalResponseDto modifyExitStatus(Long childId, User user){
        UserValidator.validateTeacherAndPrincipal(user);

        Attendance attendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now()).orElseThrow(
                () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
        );
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CHILD_NOT_FOUND)
        );
        String accessToken = user.getAccessToken();
        String kindergartenName = child.getUser().getKindergarten().getKindergartenName();
        String childName = child.getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String enterTime;
        String exitTime;
        Long kakaoId = child.getUser().getKakaoId();

        if(attendance.getExitTime() == null){
            attendance.exit(LocalTime.now(), 하원);
            enterTime = attendance.getEnterTime().format(formatter);
            exitTime = attendance.getExitTime().format(formatter);
            Boolean flag = messageService.sendToFriendMessage(하원,accessToken, kindergartenName, childName, enterTime, exitTime, kakaoId);
            if(flag == false){
                return GlobalResponseDto.from(MESSAGE_NOT_TRANSPORT);
            }
            else
                return GlobalResponseDto.from(CustomStatusCode.CHILD_EXIT_SUCCESS);
        }

        else {
            attendance.exit(null, 등원);
            Boolean flag = messageService.sendToFriendMessage(하원, accessToken, kindergartenName, childName, kakaoId);
            if(flag == false){
                return GlobalResponseDto.from(MESSAGE_NOT_TRANSPORT);
            }
            else
                return GlobalResponseDto.from(CHILD_EXIT_CANCEL);
        }
    }

    // 해당 반의 월별 출결 내역 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findAttendanceMonth(Long classroomId, Long kindergartenId, int year, int month, User user){
        UserValidator.validateTeacherAndPrincipal(user);
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(KINDERGARTEN_NOT_FOUND)
        );
        Classroom classroom;
        if(classroomId == -1){
            classroom = classroomRepository.findClassroomWithLowestId();
            if(classroom==null){
                return GlobalResponseDto.from(CLASSROOM_NOT_EXIST);
            }
            classroomId = classroom.getId();
        }
        else {
            classroom = classroomRepository.findById(classroomId).orElseThrow(
                    () -> new ClassroomException(CLASSROOM_NOT_FOUND)
            );
            classroomId = classroom.getId();
        }

        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<MonthAttendanceDto> monthAttendanceList = new ArrayList<>();
        for(Child child : children){
            List<DayAttendanceResponseDto> dayAttendanceList = new ArrayList<>();
            List<Attendance> attendanceList = attendanceRepository.findAttendanceListByMonthUntilToday(year, month, child.getId());
            for(Attendance attendance : attendanceList){
                Day day = getDay(attendance);
                dayAttendanceList.add(DayAttendanceResponseDto.of(attendance, day));
            }
            List<Attendance> enteredAttendance = attendanceRepository.findByStatusAndChildIdAndMonthAndYearUntilToday(출석, child.getId(), month, year);
            int attendanceCount = enteredAttendance.size();
            List<Attendance> absentedAttendance = attendanceRepository.findByStatusAndChildIdAndMonthAndYearUntilToday(결석, child.getId(), month, year);
            int absentedCount = absentedAttendance.size();
            monthAttendanceList.add(MonthAttendanceDto.of(child, dayAttendanceList, attendanceCount, absentedCount));
        }

        List<ClassroomInfoDto> everyClass = new ArrayList<>();
        List<Classroom> classroomList = classroomRepository.findByKindergartenId(kindergartenId);
        for(Classroom found : classroomList){
            everyClass.add(ClassroomInfoDto.of(found.getId(), found.getName()));
        }
        return GlobalResponseDto.of(MONTH_ATTENDANCE_LIST_SUCCESS, MonthAttendanceResponseDto.of(everyClass, monthAttendanceList));

    }

    // 반 별 해당 날짜의 출결 내역 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findAttendanceDate(Long classroomId, Long kindergartenId, String date, User user){
        UserValidator.validateTeacherAndPrincipal(user);
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(KINDERGARTEN_NOT_FOUND)
        );
        Classroom classroom;
        if(classroomId == -1){
            classroom = classroomRepository.findClassroomWithLowestId();
            if(classroom==null){
                return GlobalResponseDto.from(CLASSROOM_NOT_EXIST);
            }
            classroomId = classroom.getId();
        }
        else {
            classroom = classroomRepository.findById(classroomId).orElseThrow(
                    () -> new ClassroomException(CLASSROOM_NOT_FOUND)
            );
            classroomId = classroom.getId();
        }
        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<DateAttendanceResponseDto> attendanceResponseDtoList = new ArrayList<>();
        for(Child child : children){
            DateAttendanceResponseDto dateAttendanceResponseDto = childRepository.findDateAttendance(LocalDate.parse(date), child.getId());
            attendanceResponseDtoList.add(dateAttendanceResponseDto);
        }

        List<ClassroomInfoDto> everyClass = new ArrayList<>();
        List<Classroom> classroomList = classroomRepository.findAllByOrderByIdAscAndKindergartenId(kindergartenId);
        for(Classroom found : classroomList){
            everyClass.add(ClassroomInfoDto.of(found.getId(), found.getName()));
        }
        return GlobalResponseDto.of(DATE_ATTENDANCE_LIST_SUCCESS, DateResponseDto.of(attendanceResponseDtoList,everyClass));
    }

    // 결석 신청
    @Transactional
    public GlobalResponseDto addAbsent(@PathVariable Long childId, AbsentAddRequestDto absentAddRequestDto, User user) {
        UserValidator.validateParent(user);
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        LocalDate startDate = absentAddRequestDto.getStartDate();
        LocalDate endDate = absentAddRequestDto.getEndDate();
        List<Attendance> localDateList = new ArrayList<>();
        if (startDate.isBefore(LocalDate.now()))
            throw new AttendanceException(INVALID_ABSENT_ADD_REQUEST);
        else if (startDate.isAfter(endDate)) {
            throw new AttendanceException(INVALID_ABSENT_ADD_REQUEST);
        }
        else if(startDate.getDayOfWeek().getValue()==7){
            throw new AttendanceException(HOLIDAY_ABSENT_NOT_ADD);
        }
        else if(endDate.getDayOfWeek().getValue()==7){
            throw new AttendanceException(HOLIDAY_ABSENT_NOT_ADD);
        }

        // 오늘부터(평일)
        if ((startDate.isEqual(LocalDate.now()) && (LocalDate.now().getDayOfWeek().getValue() != 7))) {
            Attendance attendance = attendanceRepository.findByChildAndDate(child, LocalDate.now()).orElseThrow(
                    () -> new AttendanceException(INVALID_ABSENT_ADD_REQUEST)
            );
            if (attendance.getStatus() == 결석) {
                throw new AttendanceException(INVALID_ABSENT_ADD_REQUEST);
            }
            attendance.update(결석, null, null, absentAddRequestDto.getReason());
            startDate = startDate.plusDays(1);
        }


        while ((endDate.isAfter(startDate)) || (endDate.isEqual(startDate))) {
            if (!attendanceRepository.findByChildAndDate(child, startDate).isEmpty()) {
                throw new AttendanceException(INVALID_ABSENT_ADD_REQUEST);
            } else {
                if (startDate.getDayOfWeek().getValue() != 7) {
                    localDateList.add(Attendance.of(child, 결석, startDate, absentAddRequestDto.getReason()));
                }
                startDate = startDate.plusDays(1);
            }
        }
            attendanceRepository.saveAll(localDateList);
            absentInfoRepository.save(AbsentInfo.of(absentAddRequestDto, child));
            return GlobalResponseDto.from(ADD_ABSENT_SUCCESS);
    }

    // 결석 취소
    @Transactional
    public GlobalResponseDto cancelAbsent(Long childId, Long absentInfoId, User user){
        UserValidator.validateParent(user);

        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );

        AbsentInfo absentInfo = absentInfoRepository.findById(absentInfoId).orElseThrow(
                () -> new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST)
        );
        absentInfoRepository.deleteById(absentInfo.getId());
        LocalDate startDate = absentInfo.getStartDate();
        LocalDate endDate = absentInfo.getEndDate();

        if(startDate.isAfter(endDate)){
            throw new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST);
        }
        else if(startDate.isBefore(LocalDate.now())&&endDate.isBefore(LocalDate.now())){
            throw new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST);
        }
        else if(startDate.isBefore(LocalDate.now())&&endDate.isEqual(LocalDate.now())){
            Attendance attendance = attendanceRepository.findByChildAndDate(child, LocalDate.now()).orElseThrow(
                    () -> new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST)
            );
            attendance.update(미등원, null, null, null);
        }
        else if(startDate.isBefore(LocalDate.now())&&endDate.isAfter(LocalDate.now())){
            Attendance todayAttendance = attendanceRepository.findByChildAndDate(child, LocalDate.now()).orElseThrow(
                    () -> new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST)
            );
            todayAttendance.update(미등원, null, null, null);
            startDate = LocalDate.now().plusDays(1);
            while(!startDate.isAfter(endDate)){
                if(startDate.getDayOfWeek().getValue()==7){
                    startDate = startDate.plusDays(1);
                    continue;
                }
                Attendance attendance = attendanceRepository.findByChildAndDate(child, startDate).orElseThrow(
                        () -> new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST)
                );
                attendanceRepository.delete(attendance);
                startDate = startDate.plusDays(1);
            }

        }
        else if(startDate.isEqual(LocalDate.now())&&endDate.isEqual(LocalDate.now())){
            Attendance attendance = attendanceRepository.findByChildAndDate(child, LocalDate.now()).orElseThrow(
                    () -> new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST)
            );
            attendance.update(미등원, null, null, null);
        }
        else if(startDate.isEqual(LocalDate.now())&&endDate.isAfter(LocalDate.now())){
            Attendance todayAttendance = attendanceRepository.findByChildAndDate(child, LocalDate.now()).orElseThrow(
                    () -> new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST)
            );
            todayAttendance.update(미등원, null ,null, null);
            startDate = startDate.plusDays(1);
            while(!startDate.isAfter(endDate)){
                if(startDate.getDayOfWeek().getValue()==7){
                    startDate = startDate.plusDays(1);
                    continue;
                }
                Attendance attendance = attendanceRepository.findByChildAndDate(child, startDate).orElseThrow(
                        () -> new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST)
                );
                attendanceRepository.delete(attendance);
                startDate = startDate.plusDays(1);
            }
        }
        else if(startDate.isAfter(LocalDate.now())){
            while(!startDate.isAfter(endDate)){
                if(startDate.getDayOfWeek().getValue()==7){
                    startDate= startDate.plusDays(1);
                    continue;
                }
                Attendance attendance = attendanceRepository.findByChildAndDate(child, startDate).orElseThrow(
                        () -> new AttendanceException(INVALID_ABSENT_CANCEL_REQUEST)
                );
                attendanceRepository.delete(attendance);
                startDate = startDate.plusDays(1);
            }
        }

        return GlobalResponseDto.from(DELETE_ABSENT_SUCCESS);
    }

    // 자녀의 월별 출결 내역 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findChildAttendanceMonth(Long childId, int year, int month, User user){
        UserValidator.validateParent(user);

        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CHILD_NOT_FOUND)
        );
        List<ContentDto> contentDtoList = new ArrayList<>();
        List<AbsentInfoDto> absentInfoDtoList = new ArrayList<>();

        List<Attendance> attendanceList = attendanceRepository.findByStatusAndChildIdAndMonthAndYear(출석, child.getId(), month, year);
        List<Attendance> absentList = attendanceRepository.findByStatusAndChildIdAndMonthAndYear(결석, child.getId(), month, year);
        System.out.println("attendanceList = " + attendanceList.size());
        System.out.println("absentList.size() = " + absentList.size());
        InfoDto infoDto = new InfoDto(attendanceList.size(), absentList.size());

        List<Attendance> monthAttendanceList = attendanceRepository.findAttendanceListByMonth(year, month, child.getId());
        for(Attendance attendance : monthAttendanceList){
            contentDtoList.add(ContentDto.from(attendance));
        }

        List<AbsentInfo> absentInfoList = absentInfoRepository.findAbsentInfoByStartDateAndEndDateAndChildId(month, child.getId());

        for(AbsentInfo absentInfo : absentInfoList){
            absentInfoDtoList.add(AbsentInfoDto.from(absentInfo));
        }
        return GlobalResponseDto.of(CHILD_MONTH_ATTENDANCE_SUCCESS, ChildMonthResponseDto.of(infoDto, absentInfoDtoList, contentDtoList));
    }

    private static Day getDay(Attendance attendance) {
        Day day = null;
        switch(attendance.getDate().getDayOfWeek().getValue()) {
            case 1:
                day = 월;
                break;
            case 2:
                day = 화;
                break;
            case 3:
                day = 수;
                break;
            case 4:
                day = 목;
                break;
            case 5:
                day = 금;
                break;
            case 6:
                day = 토;
                break;
            case 7:
                day = 일;
                break;
        }
        return day;
    }

}
