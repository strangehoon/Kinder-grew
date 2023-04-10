package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.global.enumType.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ChildProfileResponseDto {
    private Long childId;
    private String name;
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String significant;
    private String profileImageUrl;

    @Builder
    public ChildProfileResponseDto(Child child) {
        this.childId = child.getId();
        this.name = child.getName();
        this.gender = child.getGender();
        this.birth = child.getBirth();
        this.significant = child.getSignificant();
        this.profileImageUrl = child.getProfileImageUrl();
    }

    public static ChildProfileResponseDto from(Child child) {
        return ChildProfileResponseDto.builder()
                .child(child)
                .build();
    }
}
