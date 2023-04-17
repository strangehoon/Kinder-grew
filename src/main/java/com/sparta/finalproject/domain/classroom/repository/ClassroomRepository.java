package com.sparta.finalproject.domain.classroom.repository;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    List<Classroom> findByKindergartenId(Long kindergartenId);

    @Query("select c from classroom c where c.id = (select MIN(c2.id) from classroom c2)")
    Classroom findClassroomWithLowestId();

}
