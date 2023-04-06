package com.sparta.finalproject.domain.child.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class CustomPageImpl<T> extends PageImpl<T> {
    private final InfoDto info;

    @JsonCreator
    public CustomPageImpl(List content, Pageable pageable, long total, InfoDto info) {
        super(content,pageable.getSort().isSorted() ? pageable : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending()), total);
        this.info = info;
    }

    @JsonCreator
    public CustomPageImpl(List content, InfoDto info) {
        super(content);
        this.info = info;
    }

    @JsonGetter(value = "content")
    @Override
    public List getContent() {
        return super.getContent();
    }

    @JsonGetter(value = "info")
    public InfoDto getPaging() {
        return info;
    }

}

