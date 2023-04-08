package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.user.dto.ParentProfileResponseDto;
import com.sparta.finalproject.domain.user.dto.ParentResponseDto;
import com.sparta.finalproject.global.enumType.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChildResponseDto {
    private Long childId;
    private String name;
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String significant;
    private String profileImageUrl;
    private ParentResponseDto parentResponseDto;
    private List<ParentResponseDto> parent;
    private ParentProfileResponseDto parentProfileResponseDto;



    @Builder
    private ChildResponseDto(Child child, ParentResponseDto parentResponseDto, List<ParentResponseDto> parent,ParentProfileResponseDto parentProfileResponseDto) {
        this.name = child.getName();
        this.profileImageUrl = child.getProfileImageUrl();
        this.childId = child.getId();
        this.birth = getBirth();
        this.significant = getSignificant();
        this.gender = getGender();
        this.parentProfileResponseDto = parentProfileResponseDto;
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

    public static ChildResponseDto of(Child child,ParentProfileResponseDto parentProfileResponseDto) {
        return ChildResponseDto.builder()
                .child(child)
                .parentProfileResponseDto(parentProfileResponseDto)
                .build();
    }
}