package com.sparta.finalproject.domain.attendance.repository;

import com.sparta.finalproject.domain.attendance.entity.AbsentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbsentInfoRepository extends JpaRepository<AbsentInfo, Long> {

    @Query("SELECT a FROM AbsentInfo a WHERE (MONTH(a.startDate) = :month OR MONTH(a.endDate) = :month) AND a.child.id =:id")
    List<AbsentInfo> findAbsentInfoByStartDateAndEndDateAndChildId(@Param("month") int month, @Param("id") Long id);
}
