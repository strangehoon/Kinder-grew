package com.sparta.finalproject.domain.gallery.controller;

import com.sparta.finalproject.domain.gallery.dto.ImagePostRequestDto;
import com.sparta.finalproject.domain.gallery.service.ImagePostService;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImagePostController {

    private final ImagePostService imagePostService;

    // 사진 게시글 생성
    @PostMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/gallery")
    public GlobalResponseDto imagePostAdd(@PathVariable Long kindergartenId, @PathVariable Long classroomId,
                                          @ModelAttribute ImagePostRequestDto imagePostRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return imagePostService.addImagePost(kindergartenId, classroomId, imagePostRequestDto, userDetails.getUser());
    }

    // 사진 게시글 조회
    @GetMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/gallery/{imagePostId}")
    public GlobalResponseDto imagePostFind(@PathVariable Long kindergartenId, @PathVariable Long classroomId,
                                           @PathVariable Long imagePostId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return imagePostService.findImagePost(kindergartenId, imagePostId, userDetails.getUser());
    }

    // 사진 게시글 검색
    @GetMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/gallery")
    public GlobalResponseDto imagePostPageFind(@PathVariable Long kindergartenId, @PathVariable Long classroomId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestParam(required = false, defaultValue = "2000-01-01", value = "start") String start,
                                               @RequestParam(required = false, defaultValue = "3000-01-01", value = "end") String end,
                                               @RequestParam(required = false, defaultValue = "", value = "keyword") String keyword,
                                               @RequestParam(required = false, defaultValue = "1", value = "page") int page,
                                               @RequestParam(required = false, defaultValue = "0", value = "isAsc") boolean isAsc) {
        return imagePostService.findImagePostPage(kindergartenId, classroomId,userDetails.getUser(),start, end, keyword, page-1, isAsc);
    }

//    // 사진 게시글 수정
//    @PutMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/gallery/{imagePostId}")
//    public GlobalResponseDto imagePostModify(@PathVariable Long kindergartenId, @PathVariable Long classroomId,
//                                             @PathVariable Long imagePostId,
//                                             @ModelAttribute ImagePostRequestDto imagePostRequestDto,
//                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{
//        return imagePostService.modifyImagePost(kindergartenId, imagePostId, imagePostRequestDto, userDetails.getUser());
//    }

    // 사진 게시글 삭제
    @DeleteMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/gallery/{imagePostId}")
    public GlobalResponseDto imagePostDelete(@PathVariable Long kindergartenId, @PathVariable Long classroomId,
                                             @PathVariable Long imagePostId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return imagePostService.deleteImagePost(kindergartenId, imagePostId, userDetails.getUser());
    }
}
