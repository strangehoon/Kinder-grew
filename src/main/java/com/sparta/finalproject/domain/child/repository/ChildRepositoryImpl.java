package com.sparta.finalproject.domain.child.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.finalproject.domain.child.dto.*;
import com.sparta.finalproject.global.enumType.CommuteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

import static com.sparta.finalproject.domain.attendance.entity.QAttendance.attendance;
import static com.sparta.finalproject.domain.child.entity.QChild.child;
import static com.sparta.finalproject.global.enumType.AttendanceStatus.결석;
import static com.sparta.finalproject.global.enumType.CommuteStatus.ENTER;
import static com.sparta.finalproject.global.enumType.CommuteStatus.EXIT;


@RequiredArgsConstructor
@Repository
public class ChildRepositoryImpl implements ChildRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChildScheduleResponseDto> findChildSchedule(Long classroomId, CommuteStatus commuteStatus, String time, Pageable pageable, InfoDto info){
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
                .where(classIdIs(classroomId), stateIs(commuteStatus),
                        timeIs(commuteStatus, time), attendance.date.eq(LocalDate.now()))
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
                .where(classIdIs(classroomId), stateIs(commuteStatus),
                        timeIs(commuteStatus, time), attendance.date.eq(LocalDate.now()))
                .orderBy(child.name.asc())
                .fetchCount();
        List<ChildScheduleResponseDto> contents = result.getResults();


        return new CustomPageImpl<>(contents, pageable, total, info);
    }

    private BooleanExpression classIdIs(Long classroomId) {
        return classroomId != null ? child.classroom.id.eq(classroomId) : null;
    }
    private BooleanExpression stateIs(CommuteStatus commuteStatus){
        if(commuteStatus.equals(ENTER)){
            return attendance.exitTime.isNull().and(attendance.date.eq(LocalDate.now())).and(attendance.status.ne(결석));
        }
        else if(commuteStatus.equals(EXIT)) {
            return attendance.enterTime.isNotNull().and(attendance.date.eq(LocalDate.now())).and(attendance.status.ne(결석));
        }
        else
            return null;
    }

    private BooleanExpression timeIs(CommuteStatus commuteStatus, String time) {
        if(commuteStatus.equals(ENTER)){
            return time != null ? child.dailyEnterTime.eq(time) : null;
        }
        else
            return time != null ? child.dailyExitTime.eq(time) : null;
    }


}
