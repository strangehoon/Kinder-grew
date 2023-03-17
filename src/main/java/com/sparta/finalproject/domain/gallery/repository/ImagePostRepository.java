package com.sparta.finalproject.domain.gallery.repository;

import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ImagePostRepository extends JpaRepository<ImagePost, Long> {
    List<ImagePost> findAllByClassroomIdAndCreatedAtBetween(Long id, LocalDate startDate, LocalDate endDate);
}
