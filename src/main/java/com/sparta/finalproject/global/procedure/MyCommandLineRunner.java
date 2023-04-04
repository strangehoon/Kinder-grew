//package com.sparta.finalproject.global.procedure;
//
//import com.sparta.finalproject.domain.attendance.entity.Attendance;
//import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//
//@Component
//@RequiredArgsConstructor
//public class MyCommandLineRunner implements CommandLineRunner {
//    private final MyStoredProcService myStoredProcService;
//    private final AttendanceRepository attendanceRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 프로시저 호출
//        myStoredProcService.callMyStoredProc();
//
////        Attendance attendance = attendanceRepository.findByChildIdAndDate(1l, LocalDate.parse("2023-01-04"));
////        attendance.
//    }
//}
