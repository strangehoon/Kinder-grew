package com.sparta.finalproject.domain.child.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.sparta.finalproject.domain.classroom.dto.ClassroomInfoDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class CustomPageImpl<T> extends PageImpl<T> {
    private final InfoDto info;
    private List<ClassroomInfoDto> everyClass;

    @JsonCreator
    public CustomPageImpl(List<T> content, Pageable pageable, long total, InfoDto info, List<ClassroomInfoDto> everyClass) {
        super(content,pageable.getSort().isSorted() ? pageable : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("createdAt").descending()), total);
        this.info = info;
        this.everyClass = everyClass;
    }

    @JsonCreator
    public CustomPageImpl(List<T> content, InfoDto info) {
        super(content);
        this.info = info;
    }

    @JsonGetter(value = "content")
    @Override
    public List<T> getContent() {
        return super.getContent();
    }

    @JsonGetter(value = "info")
    public InfoDto getPaging() {
        return info;
    }

    @JsonGetter(value = "everyClass")
    public List<ClassroomInfoDto> getEveryClass() {
        return everyClass;
    }

    public void setEveryClass(List<ClassroomInfoDto> everyClass) {
        this.everyClass = everyClass;
    }
}
