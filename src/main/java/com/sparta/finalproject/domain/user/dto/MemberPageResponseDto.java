package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.global.enumType.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class MemberPageResponseDto {
    private UserRoleEnum userRole;
    private Long memberCount;
    private List<MemberResponseDto> memberList;
    private List<MemberResponseDto> earlyMemberList;

    @Builder
    private MemberPageResponseDto(UserRoleEnum userRole, Long memberCount, List<MemberResponseDto> memberList, List<MemberResponseDto> earlyMemberList){
        this.userRole = userRole;
        this.memberCount = memberCount;
        this.memberList = memberList;
        this.earlyMemberList = earlyMemberList;
    }

    public static MemberPageResponseDto of(UserRoleEnum userRole,Long teacherCount, List<MemberResponseDto> memberList, List<MemberResponseDto> earlyMemberList){
        return MemberPageResponseDto.builder()
                .userRole(userRole)
                .memberCount(teacherCount)
                .memberList(memberList)
                .earlyMemberList(earlyMemberList)
                .build();
    }
}
