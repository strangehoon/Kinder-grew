package com.sparta.finalproject.domain.gallery.service;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.gallery.dto.ImagePostRequestDto;
import com.sparta.finalproject.domain.gallery.dto.ImagePostResponseDto;
import com.sparta.finalproject.domain.gallery.entity.Image;
import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import com.sparta.finalproject.domain.gallery.repository.ImagePostRepository;
import com.sparta.finalproject.domain.gallery.repository.ImageRepository;
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagePostService {

    private final ImagePostRepository imagePostRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    private final ClassroomRepository classroomRepository;

    public ResponseEntity<ImagePostResponseDto> createImagePost(Long classroom_id, ImagePostRequestDto imagePostRequestDto, List<MultipartFile> multipartFilelist) throws IOException {
        Classroom classroom = classroomRepository.findById(classroom_id).orElseThrow(
                () -> new IllegalArgumentException("반을 찾을 수 없습니다.")
        );
        ImagePost imagePost = imagePostRepository.saveAndFlush(ImagePost.of(imagePostRequestDto, classroom));
        if (multipartFilelist != null) {
            s3Service.upload(multipartFilelist, "gallery", imagePost);
        }
        Image image = imageRepository.findFirstByImagePost(imagePost);
        String thumbnailImageUrl = s3Service.getThumbnailPath(image.getImageUrl());
        return ResponseEntity.ok(ImagePostResponseDto.of(imagePost, thumbnailImageUrl));
    }

    public ResponseEntity<List<ImagePostResponseDto>> getImagePosts(Long classroomId) {
        List<ImagePost> imagePostList = imagePostRepository.findAllByClassroomId(classroomId);
        List<ImagePostResponseDto> responseDtoList = new ArrayList<>();
        for (ImagePost imagePost : imagePostList){
            Image image = imageRepository.findFirstByImagePost(imagePost);
            String thumbnailImageUrl = s3Service.getThumbnailPath(image.getImageUrl());
            responseDtoList.add(ImagePostResponseDto.of(imagePost, thumbnailImageUrl));
        }
        return ResponseEntity.ok(responseDtoList);
    }
}
