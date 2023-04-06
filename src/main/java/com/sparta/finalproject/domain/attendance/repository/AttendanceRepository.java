package com.sparta.finalproject.domain.attendance.repository;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.global.enumType.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByChildIdAndDate(Long childId, LocalDate date);
    Optional<Attendance> findByChildAndDate(Child child, LocalDate date);

    Optional<Attendance> findByStatusAndDate(Status status, LocalDate date);

//    Long countByChild_Classroom_IdAndDateAndEnteredIsTrue(Long classroomId, LocalDate date);
//    Long countByChild_Classroom_IdAndDateAndEnteredIsFalse(Long classroomId, LocalDate date);
//    Long countByChild_Classroom_IdAndDateAndExitedIsTrue(Long classroomId, LocalDate date);
//    Long countByDateAndEnteredIsTrue(LocalDate now);
//    Long countByDateAndEnteredIsFalse(LocalDate now);
//    Long countByDateAndExitedIsTrue(LocalDate now);
}
