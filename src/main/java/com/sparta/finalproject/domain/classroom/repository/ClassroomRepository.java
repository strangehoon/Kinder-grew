package com.sparta.finalproject.domain.classroom.repository;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    List<Classroom> findByKindergartenId(Long kindergartenId);
}
