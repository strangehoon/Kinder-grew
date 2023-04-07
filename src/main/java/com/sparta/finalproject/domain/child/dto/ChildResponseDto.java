package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.user.dto.ParentResponseDto;
import com.sparta.finalproject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChildResponseDto {
    private Long childId;
    private String name;
    private String profileImageUrl;
    private ParentResponseDto parentResponseDto;
    private List<ParentResponseDto> parent;



    @Builder
    private ChildResponseDto(Child child, ParentResponseDto parentResponseDto, List<ParentResponseDto> parent) {
        if (child != null) {
            this.name = child.getName();
            this.profileImageUrl = child.getProfileImageUrl();
            this.childId = child.getId();
        } else {
            this.name = "";
            this.profileImageUrl = "";
            this.childId = 0L;
        }
        this.parentResponseDto = parentResponseDto;
        this.parent = parent;
    }

    public static ChildResponseDto of(Child child) {
        return ChildResponseDto.builder()
                .child(child)
                .build();
    }

    public static ChildResponseDto of(Child child, List<ParentResponseDto> parent) {
        return ChildResponseDto.builder()
                .child(child)
                .parent(parent)
                .build();
    }

    public static ChildResponseDto from(List<ParentResponseDto> parent) {
        return ChildResponseDto.builder()
                .parent(parent)
                .build();
    }

    public static ChildResponseDto of(Child child,ParentResponseDto parentResponseDto) {
        return ChildResponseDto.builder()
                .child(child)
                .parentResponseDto(parentResponseDto)
                .build();
    }

    public static ChildResponseDto of(ParentResponseDto parentResponseDto) {
        return ChildResponseDto.builder()
                .parentResponseDto(parentResponseDto)
                .build();
    }

}