package com.sparta.finalproject.domain.gallery.repository;

import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagePostRepository extends JpaRepository<ImagePost, Long> {
    List<ImagePost> findAllByClassroomId(Long id);
}
