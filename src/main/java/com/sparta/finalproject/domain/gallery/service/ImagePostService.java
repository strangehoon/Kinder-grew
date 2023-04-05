package com.sparta.finalproject.domain.gallery.service;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.gallery.dto.GalleryPageResponseDto;
import com.sparta.finalproject.domain.gallery.dto.ImagePostRequestDto;
import com.sparta.finalproject.domain.gallery.dto.ImagePostResponseDto;
import com.sparta.finalproject.domain.gallery.entity.Image;
import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import com.sparta.finalproject.domain.gallery.repository.ImagePostRepository;
import com.sparta.finalproject.domain.gallery.repository.ImageRepository;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import com.sparta.finalproject.global.response.exceptionType.ImagePostException;
import com.sparta.finalproject.global.response.exceptionType.UserException;
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagePostService {

    private final ImagePostRepository imagePostRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    private final ClassroomRepository classroomRepository;
    private static final int PAGE_NUMBER = 15;

    @Transactional
    public GlobalResponseDto imagePostAdd(Long classroom_id, ImagePostRequestDto imagePostRequestDto, User user) throws IOException {
        if(user.getRole() != UserRoleEnum.ADMIN){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        Classroom classroom = classroomRepository.findById(classroom_id).orElseThrow(
                () -> new ClassroomException(CustomStatusCode.CLASSROOM_NOT_FOUND)
        );
        ImagePost imagePost = imagePostRepository.save(ImagePost.of(imagePostRequestDto, classroom));
        List<MultipartFile> imageList = imagePostRequestDto.getImageList();
        if (imageList != null) {
            s3Service.upload(imageList, "gallery", imagePost);
        }
        Image image = imageRepository.findFirstByImagePost(imagePost);
        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(image.getImageUrl());
        return GlobalResponseDto.of(CustomStatusCode.ADD_IMAGE_POST_SUCCESS, ImagePostResponseDto.of(imagePost, imageUrlList));
    }

    @Transactional(readOnly = true)
    public GlobalResponseDto imagePostFind(Long imagePostId){
        ImagePost imagePost = imagePostRepository.findById(imagePostId).orElseThrow(
                () -> new ImagePostException(CustomStatusCode.IMAGE_POST_NOT_FOUND)
        );
        List<Image> imageList = imageRepository.findAllByImagePost(imagePost);
        List<String> imageUrlList = new ArrayList<>();
        for (Image image : imageList) {
            imageUrlList.add(image.getImageUrl());
        }
        return GlobalResponseDto.of(CustomStatusCode.FIND_IMAGE_LIST_SUCCESS, ImagePostResponseDto.of(imagePost, imageUrlList));
    }

    @Transactional
    public GlobalResponseDto imagePostDelete(Long imagePostId, User user) {
        if(user.getRole() != UserRoleEnum.ADMIN){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        List<Image> imagePostList = imageRepository.findAllByImagePostId(imagePostId);
        for (Image image : imagePostList) {
            s3Service.deleteFile(image.getImageUrl().substring(63));
        }
        try {
            imageRepository.deleteAllByImagePostId(imagePostId);
            imagePostRepository.deleteById(imagePostId);
        } catch (Exception e) {
            throw new ImagePostException(CustomStatusCode.IMAGE_POST_NOT_FOUND);
        }
        return GlobalResponseDto.from(CustomStatusCode.DELETE_IMAGE_POST_SUCCESS);
    }

    public GlobalResponseDto imagePostPageFind(Long classroomId, String start, String end, String keyword, int page, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "id");
        Pageable pageable = PageRequest.of(page, PAGE_NUMBER, sort);
        Page<ImagePost> imagePostList = imagePostRepository.findAllByClassroomIdAndTitleIsContainingAndCreatedAtBetween(classroomId, keyword, LocalDate.parse(start), LocalDate.parse(end), pageable);
        Long imagePostCount = imagePostRepository.countByClassroomIdAndTitleIsContainingAndCreatedAtBetween(classroomId, keyword, LocalDate.parse(start), LocalDate.parse(end));
        List<ImagePostResponseDto> responseDtoList = new ArrayList<>();
        for (ImagePost imagePost : imagePostList) {
            Image image = imageRepository.findFirstByImagePost(imagePost);
            List<String> imageUrlList = new ArrayList<>();
            imageUrlList.add(image.getImageUrl());
            responseDtoList.add(ImagePostResponseDto.of(imagePost, imageUrlList));
        }
        return GlobalResponseDto.of(CustomStatusCode.FIND_IMAGE_POST_PAGE_SUCCESS,
                GalleryPageResponseDto.of(imagePostCount, responseDtoList));

    }

    public GlobalResponseDto imagePostModify(Long imagePostId, ImagePostRequestDto imagePostRequestDto, User user) throws IOException{
        if(user.getRole() != UserRoleEnum.ADMIN){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        ImagePost imagePost = imagePostRepository.findById(imagePostId).orElseThrow(
                () -> new ImagePostException(CustomStatusCode.IMAGE_POST_NOT_FOUND)
        );
        imagePost.update(imagePostRequestDto);
        imageRepository.deleteAllByImagePostId(imagePostId);
        List<MultipartFile> imageList = imagePostRequestDto.getImageList();
        if (imageList != null) {
            s3Service.upload(imageList, "gallery", imagePost);
        }
        Image image = imageRepository.findFirstByImagePost(imagePost);
        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(image.getImageUrl());
        return GlobalResponseDto.of(CustomStatusCode.MODIFY_IMAGE_POST_SUCCESS,
                ImagePostResponseDto.of(imagePost, imageUrlList));
    }
}
