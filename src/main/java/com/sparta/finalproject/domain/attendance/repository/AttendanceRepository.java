package com.sparta.finalproject.domain.attendance.repository;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.child.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByChildIdAndDate(Long childId, LocalDate date);
    Optional<Attendance> findByChildAndDate(Child child, LocalDate date);
    Long countByChild_Classroom_IdAndDateAndEnterTimeIsNotNull(Long classroomId, LocalDate date);
    Long countByChild_Classroom_IdAndDateAndEnterTimeIsNull(Long classroomId, LocalDate date);
    Long countByChild_Classroom_IdAndDateAndExitTimeIsNotNull(Long classroomId, LocalDate date);
    Long countByDateAndEnterTimeIsNotNull(LocalDate now);
    Long countByDateAndEnterTimeIsNull(LocalDate now);
    Long countByDateAndExitTimeIsNotNull(LocalDate now);
}
