package com.sparta.finalproject.domain.absent.repository;

import com.sparta.finalproject.domain.absent.entity.AbsentInfo;
import com.sparta.finalproject.domain.child.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AbsentInfoRepository extends JpaRepository<AbsentInfo, Long> {
    List<AbsentInfo> findAllByChildAndStartDateBetween(Child child, LocalDate startDate, LocalDate endDate);

    void deleteByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
}
