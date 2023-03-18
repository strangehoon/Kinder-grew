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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImagePostService {

    private final ImagePostRepository imagePostRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    private final ClassroomRepository classroomRepository;

    @Transactional
    public ResponseEntity<ImagePostResponseDto> createImagePost(Long classroom_id, ImagePostRequestDto imagePostRequestDto, List<MultipartFile> multipartFilelist) throws IOException {
        Classroom classroom = classroomRepository.findById(classroom_id).orElseThrow(
                () -> new IllegalArgumentException("반을 찾을 수 없습니다.")
        );
        ImagePost imagePost = imagePostRepository.saveAndFlush(ImagePost.of(imagePostRequestDto, classroom));
        if (multipartFilelist != null) {
            s3Service.upload(multipartFilelist, "gallery", imagePost);
        }
        Image image = imageRepository.findFirstByImagePost(imagePost);
        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(s3Service.getThumbnailPath(image.getImageUrl()));
        return ResponseEntity.ok(ImagePostResponseDto.of(imagePost, imageUrlList));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<ImagePostResponseDto>> getImagePostListByCriteria(Long classroomId, String start, String end, String keyword) {
        List<ImagePost> imagePostList = imagePostRepository.findAllByClassroomIdAndCreatedAtBetweenOrderByIdDesc(classroomId, LocalDate.parse(start), LocalDate.parse(end));
        imagePostList = imagePostList.stream().filter(imagePost -> imagePost.getTitle().contains(keyword)).collect(Collectors.toList());
        List<ImagePostResponseDto> responseDtoList = new ArrayList<>();
        for (ImagePost imagePost : imagePostList){
            Image image = imageRepository.findFirstByImagePost(imagePost);
            List<String> imageUrlList = new ArrayList<>();
            imageUrlList.add(s3Service.getThumbnailPath(image.getImageUrl()));
            responseDtoList.add(ImagePostResponseDto.of(imagePost, imageUrlList));
        }
        return ResponseEntity.ok(responseDtoList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ImagePostResponseDto> getImagePost(Long imagePostId) {
        ImagePost imagePost = imagePostRepository.findById(imagePostId).orElseThrow(
                () -> new IllegalArgumentException("사진 게시글을 찾을 수 없습니다.")
        );
        List<Image> imageList = imageRepository.findAllByImagePost(imagePost);
        List<String> imageUrlList = new ArrayList<>();
        for (Image image : imageList){
            imageUrlList.add(image.getImageUrl());
        }
        return ResponseEntity.ok(ImagePostResponseDto.of(imagePost, imageUrlList));
    }

    @Transactional
    public String deleteImagePost(Long imagePostId) {
        imageRepository.deleteAllByImagePostId(imagePostId);
        imagePostRepository.deleteById(imagePostId);
        return "사진 게시글이 삭제되었습니다.";
    }
}
