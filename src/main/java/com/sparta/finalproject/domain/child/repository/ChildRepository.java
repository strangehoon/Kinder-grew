package com.sparta.finalproject.domain.child.repository;

import com.sparta.finalproject.domain.child.entity.Child;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
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
}
