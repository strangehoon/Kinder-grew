package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherResponseDto {
    private Long id;
    private String name;
    private String phoneNumber;

    @Builder
    private TeacherResponseDto(User teacher){
        this.id = teacher.getId();
        this.name = teacher.getName();
        this.phoneNumber = teacher.getPhoneNumber();
    }

    public static TeacherResponseDto from(User teacher){
        return builder()
                .teacher(teacher)
                .build();
    }
}
