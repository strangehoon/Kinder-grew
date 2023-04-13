package com.sparta.finalproject.domain.attendance.repository;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByChildIdAndDate(Long childId, LocalDate date);
    Optional<Attendance> findByChildAndDate(Child child, LocalDate date);

    @Query("select a from Child c join c.attendanceList a where month(a.date) = :month and year(a.date) = :year and c.id = :childId")
    List<Attendance> findAttendanceListByMonth(@Param("year") int year, @Param("month") int month, @Param("childId") Long childId);

    @Query("select a from Attendance a where a.status = :status and month(a.date) = :month and year(a.date) = :year and a.child.id = :childId and a.date < DATE(NOW())")
    List<Attendance> findByStatusAndChildIdAndMonthAndYearUntilToday(@Param("status") AttendanceStatus status, @Param("childId") Long childId, @Param("month") int month, @Param("year") int year);

    @Query("select a from Attendance a where a.status = :status and month(a.date) = :month and year(a.date) = :year and a.child.id = :childId")
    List<Attendance> findByStatusAndChildIdAndMonthAndYear(@Param("status") AttendanceStatus status, @Param("childId") Long childId, @Param("month") int month, @Param("year") int year);

}
