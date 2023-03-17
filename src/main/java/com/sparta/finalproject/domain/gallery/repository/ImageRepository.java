package com.sparta.finalproject.domain.gallery.repository;

import com.sparta.finalproject.domain.gallery.entity.Image;
import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByImagePostOrderByIdDesc(ImagePost imagePost);
    Image findFirstByImagePost(ImagePost imagePost);
}
