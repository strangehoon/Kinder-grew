package com.sparta.finalproject.domain.child.repository;

import com.sparta.finalproject.domain.child.dto.ChildScheduleResponseDto;
import com.sparta.finalproject.domain.child.dto.InfoDto;
import com.sparta.finalproject.domain.classroom.dto.ClassroomInfoDto;
import com.sparta.finalproject.global.enumType.CommuteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepositoryCustom {

    Page<ChildScheduleResponseDto> findChildSchedule(Long classroomId, Long kindergartenId, CommuteStatus commuteStatus, String time, Pageable pageable,
                                                                       InfoDto infoDto, List<ClassroomInfoDto> everyClass);
}
