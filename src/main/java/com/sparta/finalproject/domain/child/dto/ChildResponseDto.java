package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChildResponseDto {
    private Long childId;
    private String name;
    private String profileImageUrl;


    @Builder
    private ChildResponseDto(Child child) {
        this.name = child.getName();
        this.profileImageUrl = child.getProfileImageUrl();
        this.childId = child.getId();
    }

    public static ChildResponseDto of(Child child) {
        return ChildResponseDto.builder()
                .child(child)
                .build();
    }
}