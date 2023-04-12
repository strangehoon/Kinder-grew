package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.dto.*;
import com.sparta.finalproject.domain.attendance.entity.AbsentInfo;
import com.sparta.finalproject.domain.attendance.repository.AbsentInfoRepository;
import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.Day;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.AbsentException;
import com.sparta.finalproject.global.response.exceptionType.AttendanceException;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import com.sparta.finalproject.global.response.exceptionType.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.bytebuddy.asm.Advice;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.finalproject.global.enumType.AttendanceStatus.*;
import static com.sparta.finalproject.global.enumType.Day.*;
import static com.sparta.finalproject.global.enumType.UserRoleEnum.*;
import static com.sparta.finalproject.global.response.CustomStatusCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final ChildRepository childRepository;
    private final AttendanceRepository attendanceRepository;
    private final AbsentInfoRepository absentInfoRepository;

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
                attendanceList.add(Attendance.of(child, 미등원));
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


    //등원 처리
    @Transactional
    public GlobalResponseDto modifyEnterStatus(Long childId){
        Attendance attendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now()).orElseThrow(
                () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
        );
        // 등원 처리
        if(attendance.getEnterTime() == null){
            attendance.enter(LocalTime.now(), 등원);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_ENTER_SUCCESS);
        }
        // 등원 처리 취소
        else {
            attendance.enter(null, 미등원);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_ENTER_CANCEL);
        }
    }

    // 하원 처리
    @Transactional
    public GlobalResponseDto modifyExitStatus(Long childId){
        Attendance attendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now()).orElseThrow(
                () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
        );
        // 하원 처리
        if(attendance.getExitTime() == null){
            attendance.exit(LocalTime.now(), 하원);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_EXIT_SUCCESS);
        }

        // 하원 처리 취소
        else {
            attendance.exit(null, 등원);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_EXIT_CANCEL);
        }
    }

    // 해당 반의 월별 출결 내역 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findAttendanceMonth(Long classroomId, int year, int month){
        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<MonthAttendanceResponseDto> monthAttendanceList = new ArrayList<>();
        for(Child child : children){
            List<DayAttendanceResponseDto> dayAttendanceList = new ArrayList<>();
            List<Attendance> attendanceList = attendanceRepository.findAttendanceListByMonth(year, month, child.getId());
            for(Attendance attendance : attendanceList){
                Day day = getDay(attendance);
                dayAttendanceList.add(DayAttendanceResponseDto.of(attendance, day));
            }
            List<Attendance> enteredAttendance = attendanceRepository.findByStatusAndChildIdAndMonth(출석, child.getId(), month);
            int attendanceCount = enteredAttendance.size();
            List<Attendance> absentedAttendance = attendanceRepository.findByStatusAndChildIdAndMonth(결석, child.getId(), month);
            int absentedCount = absentedAttendance.size();
            monthAttendanceList.add(MonthAttendanceResponseDto.of(child, dayAttendanceList, attendanceCount, absentedCount));
        }
        return GlobalResponseDto.of(MONTH_ATTENDANCE_LIST_SUCCESS, monthAttendanceList);

    }

    // 반 별 해당 날짜의 출결 내역 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findAttendanceDate(Long classroomId, String date){
        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<DateAttendanceResponseDto> attendanceResponseDtoList = new ArrayList<>();
        for(Child child : children){
            DateAttendanceResponseDto dateAttendanceResponseDto = childRepository.findDateAttendance(LocalDate.parse(date), child.getId());
            attendanceResponseDtoList.add(dateAttendanceResponseDto);
        }
        return GlobalResponseDto.of(DATE_ATTENDANCE_LIST_SUCCESS, attendanceResponseDtoList);
    }

    // 결석 신청
    @Transactional
    public GlobalResponseDto addAbsent(@PathVariable Long childId, AbsentAddRequestDto absentAddRequestDto) {
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
    public GlobalResponseDto cancelAbsent(Long childId, Long absentInfoId){
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
        if(user.getRole() != PARENT){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }

        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CHILD_NOT_FOUND)
        );
        List<ContentDto> contentDtoList = new ArrayList<>();
        List<AbsentInfoDto> absentInfoDtoList = new ArrayList<>();

        List<Attendance> attendanceList = attendanceRepository.findByStatusAndChildIdAndMonthAndYear(출석, child.getId(), month, year);
        List<Attendance> absentList = attendanceRepository.findByStatusAndChildIdAndMonthAndYear(결석, child.getId(), month, year);
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
