package com.sparta.finalproject.domain.child.repository;

import com.sparta.finalproject.domain.child.entity.Child;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
    Optional<Child> findByClassroomIdAndName(Long classroomId, String name);
    Optional<Child> findByClassroomIdAndId(Long classroomId, Long Id);
    List<Child> findAllByClassroomId(Long classroomId);

    Page<Child> findAllByDailyEnterTimeBetween(LocalTime dailyEnterTime, LocalTime dailyExitDate, Pageable pageable);
    Page<Child> findAllByClassroomIdAndDailyEnterTimeBetween(Long classroomId, LocalTime dailyEnterTime, LocalTime dailyExitDate, Pageable pageable);

}

