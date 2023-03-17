package com.sparta.finalproject.domain.classroom.repository;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
