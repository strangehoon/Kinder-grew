package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SidebarChildrenInfo {

    private Long childId;

    private String name;

    @Builder
    private SidebarChildrenInfo(Child child) {

        this.childId = child.getId();
        this.name = child.getName();
    }

    public static SidebarChildrenInfo from(Child child) {

        return SidebarChildrenInfo.builder()
                .child(child)
                .build();
    }
}
