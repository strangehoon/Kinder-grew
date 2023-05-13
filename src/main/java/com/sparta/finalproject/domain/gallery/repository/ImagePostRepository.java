package com.sparta.finalproject.domain.gallery.repository;

import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ImagePostRepository extends JpaRepository<ImagePost, Long> {
//    List<ImagePost> findAllByClassroomIdAndCreatedAtBetweenOrderByIdDesc(Long id, LocalDate startDate, LocalDate endDate);
    Page<ImagePost> findAllByClassroomIdAndTitleIsContainingAndCreatedAtBetween(Long id, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Long countByClassroomIdAndTitleIsContainingAndCreatedAtBetween(Long id, String keyword, LocalDate startDate, LocalDate endDate);
}
