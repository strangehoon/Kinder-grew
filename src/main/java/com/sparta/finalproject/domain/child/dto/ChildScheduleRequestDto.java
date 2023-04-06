package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.global.enumType.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ChildScheduleRequestDto {

    private Long classroomId;

    private State state;

    private String time;

}
