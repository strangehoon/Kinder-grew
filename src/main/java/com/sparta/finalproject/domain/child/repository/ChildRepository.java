package com.sparta.finalproject.domain.child.repository;

import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> , ChildRepositoryCustom {
    Optional<Child> findByClassroomIdAndName(Long classroomId, String name);
    Optional<Child> findByClassroomIdAndId(Long classroomId, Long Id);
    Page<Child> findAllByClassroomId(Long classroomId, Pageable pageable);
    Page<Child> findAllByDailyEnterTime(String dailyEnterTime, Pageable pageable);
    Page<Child> findAllByClassroomIdAndDailyEnterTime(Long classroomId, String dailyEnterTime, Pageable pageable);
    Page<Child> findAllByClassroomIdAndDailyExitTime(Long classroomId, String dailyExitTime, Pageable pageable);
    Page<Child> findAllByDailyExitTime(String dailyExitTime, Pageable pageable);
    Long countByClassroomId(Long classroomId);
    Long countByClassroomIdAndDailyEnterTime(Long classroomId, String DailyEnterTime);
    Long countByClassroomIdAndDailyExitTime(Long classroomId, String DailyExitTime);
    Long countByDailyExitTime(String dailyExitTime);
    Long countByDailyEnterTime(String dailyEnterTime);

    List<Child> findByClassroomId(Long classroomOd);


    @Query("select c " + "from Child c join c.attendanceList a join c.classroom r " + "where a.date = :date and a.status = :status")
    List<Child> findAllByEntered(@Param("date") LocalDate date, @Param("status") AttendanceStatus attendanceStatus);

    List<Child> findAllByClassroomId(Long classroomId);

    @Query("select c " + "from Child c join c.attendanceList a join c.classroom r " + "where a.date = :date and a.status = :status and r.id = :classroomId")
    List<Child> findAllByEnteredAndClassroom(@Param("date") LocalDate date, @Param("status") AttendanceStatus attendanceStatus, @Param("classroomId") Long classroomId);
}
