package com.sparta.finalproject.domain.gallery.controller;

import com.sparta.finalproject.domain.gallery.dto.ImagePostRequestDto;
import com.sparta.finalproject.domain.gallery.dto.ImagePostResponseDto;
import com.sparta.finalproject.domain.gallery.service.ImagePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImagePostController {

    private final ImagePostService imagePostService;

    @PostMapping("api/managers/{classroom_id}/image-posts")
    public ResponseEntity<ImagePostResponseDto> createImagePost(@PathVariable Long classroom_id,
                                                                @RequestPart(value = "data") ImagePostRequestDto imagePostRequestDto,
                                                                @RequestPart(value = "file") List<MultipartFile> multipartFilelist) throws IOException {
        return imagePostService.createImagePost(classroom_id, imagePostRequestDto, multipartFilelist);
    }

    @GetMapping("api/common/classes/{classroom_id}/gallery")
    public ResponseEntity<List<ImagePostResponseDto>> readGallery(@PathVariable Long classroom_id){
        return imagePostService.getImagePosts(classroom_id);
    }
}
