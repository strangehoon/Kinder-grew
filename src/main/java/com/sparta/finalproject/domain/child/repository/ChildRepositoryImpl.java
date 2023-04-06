package com.sparta.finalproject.domain.child.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.finalproject.domain.child.dto.*;
import com.sparta.finalproject.global.enumType.State;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

import static com.sparta.finalproject.domain.attendance.entity.QAttendance.attendance;
import static com.sparta.finalproject.domain.child.entity.QChild.child;
import static com.sparta.finalproject.global.enumType.State.ENTER;
import static com.sparta.finalproject.global.enumType.State.EXIT;


@RequiredArgsConstructor
@Repository
public class ChildRepositoryImpl implements ChildRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChildScheduleResponseDto> findChildSchedule(ChildScheduleRequestDto requestDto, Pageable pageable, InfoDto info){
        QueryResults<ChildScheduleResponseDto> result = queryFactory
                .select(new QChildScheduleResponseDto(
                        child.id,
                        child.name,
                        child.profileImageUrl,
                        attendance.enterTime,
                        attendance.exitTime,
                        attendance.status
                ))
                .from(attendance)
                .leftJoin(attendance.child, child)
                .where(classIdIs(requestDto.getClassroomId()), stateIs(requestDto.getState()),
                        timeIs(requestDto.getState(), requestDto.getTime()), attendance.date.eq(LocalDate.now()))
                //.orderBy(attendance.status.stringValue().desc())
                .orderBy(child.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        Long total = queryFactory
                .select(new QChildScheduleResponseDto(
                        child.id,
                        child.name,
                        child.profileImageUrl,
                        attendance.enterTime,
                        attendance.exitTime,
                        attendance.status
                ))
                .from(attendance)
                .leftJoin(attendance.child, child)
                .where(classIdIs(requestDto.getClassroomId()), stateIs(requestDto.getState()),
                        timeIs(requestDto.getState(), requestDto.getTime()), attendance.date.eq(LocalDate.now()))
                //.orderBy(attendance.status.stringValue().desc())
                .orderBy(child.name.asc())
                .fetchCount();
        List<ChildScheduleResponseDto> contents = result.getResults();


        return new CustomPageImpl<>(contents, pageable, total, info);
    }

    private BooleanExpression classIdIs(Long classroomId) {
        return classroomId != null ? child.classroom.id.eq(classroomId) : null;
    }
    private BooleanExpression stateIs(State state){
        if(state.equals(ENTER)){
            return attendance.exitTime.isNull().and(attendance.date.eq(LocalDate.now()));
        }
        else if(state.equals(EXIT)) {
            return attendance.enterTime.isNotNull().and(attendance.date.eq(LocalDate.now()));
        }
        else
            return null;
    }

    private BooleanExpression timeIs(State state, String time) {
        if(state.equals(ENTER)){
            return time != null ? child.dailyEnterTime.eq(time) : null;
        }
        else
            return time != null ? child.dailyExitTime.eq(time) : null;
    }


}
