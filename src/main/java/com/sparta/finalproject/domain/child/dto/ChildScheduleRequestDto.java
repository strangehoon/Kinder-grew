package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.global.enumType.CommuteStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ChildScheduleRequestDto {

    private Long classroomId;

    private CommuteStatus commuteStatus;

    private String time;

}
