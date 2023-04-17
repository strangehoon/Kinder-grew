package com.sparta.finalproject.domain.classroom.repository;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    @Query("select c from classroom c where c.kindergarten.id = :kindergartenId order by c.id ASC")
    List<Classroom> findAllByOrderByIdAscAndKindergartenId(@Param("kindergartenId") Long kindergartenId);
    List<Classroom> findByKindergartenId(Long kindergartenId);

    @Query("select c from classroom c where c.id = (select MIN(c2.id) from classroom c2)")
    Classroom findClassroomWithLowestId();

}
