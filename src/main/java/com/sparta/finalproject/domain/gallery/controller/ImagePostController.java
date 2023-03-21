package com.sparta.finalproject.domain.gallery.controller;

import com.sparta.finalproject.domain.gallery.dto.ImagePostRequestDto;
import com.sparta.finalproject.domain.gallery.service.ImagePostService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImagePostController {

    private final ImagePostService imagePostService;

    @PostMapping("api/managers/{classroomId}/image-posts")
    public GlobalResponseDto createImagePost(@PathVariable Long classroomId,
                                             @RequestPart(value = "data") ImagePostRequestDto imagePostRequestDto,
                                             @RequestPart(value = "file") List<MultipartFile> multipartFilelist) throws IOException {
        return imagePostService.createImagePost(classroomId, imagePostRequestDto, multipartFilelist);
    }

    @GetMapping("api/common/image-posts/{imagePostId}")
    public GlobalResponseDto readImagePost(@PathVariable Long imagePostId) {
        return imagePostService.getImagePost(imagePostId);
    }

//    @GetMapping("api/common/classes/{classroomId}/gallery")
//    public ResponseEntity<List<ImagePostResponseDto>> readImagePostList(@PathVariable Long classroomId,
//                                                                        @RequestParam(required = false, defaultValue = "2000-01-01", value = "start") String startDate,
//                                                                        @RequestParam(required = false, defaultValue = "3000-01-01", value = "end") String endDate,
//                                                                        @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword) {
//        return imagePostService.getImagePostListByCriteria(classroomId, startDate, endDate, keyword);
//    }
    @GetMapping("api/common/classes/{classroomId}/gallery")
    public GlobalResponseDto readImagePostPage(@PathVariable Long classroomId,
                                               @RequestParam(required = false, defaultValue = "2000-01-01", value = "start") String startDate,
                                               @RequestParam(required = false, defaultValue = "3000-01-01", value = "end") String endDate,
                                               @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword,
                                               @RequestParam(required = false, defaultValue = "1", value = "page") int page,
                                               @RequestParam(required = false, defaultValue = "0", value = "isAsc") boolean isAsc) {
        return imagePostService.getImagePostPage(classroomId,startDate, endDate, keyword, page-1, isAsc);
    }

    @DeleteMapping("api/managers/image-posts/{imagePostId}")
    public GlobalResponseDto deleteImagePost(@PathVariable Long imagePostId) {
        return imagePostService.deleteImagePost(imagePostId);
    }
}
