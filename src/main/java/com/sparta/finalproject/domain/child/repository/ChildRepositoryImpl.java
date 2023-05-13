package com.sparta.finalproject.domain.child.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.finalproject.domain.child.dto.*;
import com.sparta.finalproject.domain.classroom.dto.ClassroomInfoDto;
import com.sparta.finalproject.global.enumType.CommuteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

import static com.sparta.finalproject.domain.attendance.entity.QAttendance.attendance;
import static com.sparta.finalproject.domain.child.entity.QChild.child;
import static com.sparta.finalproject.domain.classroom.entity.QClassroom.classroom;
import static com.sparta.finalproject.domain.kindergarten.entity.QKindergarten.kindergarten;
import static com.sparta.finalproject.global.enumType.AttendanceStatus.결석;
import static com.sparta.finalproject.global.enumType.CommuteStatus.ENTER;
import static com.sparta.finalproject.global.enumType.CommuteStatus.EXIT;


@RequiredArgsConstructor
@Repository
public class ChildRepositoryImpl implements ChildRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChildScheduleResponseDto> findChildSchedule(Long classroomId, Long kindergartenId, CommuteStatus commuteStatus, String time, Pageable pageable,
                                                                              InfoDto info, List<ClassroomInfoDto> everyClass){
        QueryResults<ChildScheduleResponseDto> result = queryFactory
                .select(new QChildScheduleResponseDto(
                        child.id,
                        child.name,
                        child.profileImageUrl,
                        attendance.enterTime,
                        attendance.exitTime,
                        attendance.status
                ))
                .from(attendance, child, classroom)
                .join(attendance.child, child)
                .join(child.classroom, classroom)
                .join(classroom.kindergarten, kindergarten)
                .where(classroomIdAndKindergartenIdIs(classroomId, kindergartenId), stateIs(commuteStatus),
                        timeIs(commuteStatus, time), attendance.date.eq(LocalDate.now()))
                .orderBy(child.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
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
                .from(attendance, child, classroom)
                .join(attendance.child, child)
                .join(child.classroom, classroom)
                .join(classroom.kindergarten, kindergarten)
                .where(classroomIdAndKindergartenIdIs(classroomId, kindergartenId), stateIs(commuteStatus),
                        timeIs(commuteStatus, time), attendance.date.eq(LocalDate.now()))
                .orderBy(child.name.asc())
                .distinct()
                .fetchCount();
        List<ChildScheduleResponseDto> contents = result.getResults();


        return new CustomPageImpl<>(contents, pageable, total, info, everyClass);
    }

    private BooleanExpression classroomIdAndKindergartenIdIs(Long classroomId, Long kindergartenId) {
        return classroomId != null ? child.classroom.id.eq(classroomId).and(classroom.kindergarten.id.eq(kindergartenId)) : classroom.kindergarten.id.eq(kindergartenId);
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
