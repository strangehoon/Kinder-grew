package com.sparta.finalproject.domain.classroom.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ClassroomResponseDto {

    private final TeacherResponseDto teacher;
    private final Long classId;

    // 원래 아이들 목록도 같이 가져와서 반환
    // private List<Child> childList = new ArrayList<>();

    @Builder
    private ClassroomResponseDto(TeacherResponseDto teacher, Long classId){
        this.teacher =teacher;
        this.classId = classId;
    }

    public static ClassroomResponseDto of(Long classId){
        return ClassroomResponseDto.builder()
                .classId(classId)
                .build();
    }

    public static ClassroomResponseDto of(TeacherResponseDto teacher, Long classId){
        return ClassroomResponseDto.builder()
                .teacher(teacher)
                .classId(classId)
                .build();
    }


}
