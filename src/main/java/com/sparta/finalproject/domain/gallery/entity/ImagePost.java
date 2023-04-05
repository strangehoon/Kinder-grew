package com.sparta.finalproject.domain.gallery.entity;

import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.gallery.dto.ImagePostRequestDto;
import com.sparta.finalproject.global.util.TimeStamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ImagePost extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @ManyToOne
    private Classroom classroom;

    @Builder
    private ImagePost(ImagePostRequestDto imagePostRequestDto, Classroom classroom) {
        this.title = imagePostRequestDto.getTitle();
        this.classroom = classroom;
    }

    public static ImagePost of(ImagePostRequestDto imagePostRequestDto, Classroom classroom){
        return ImagePost.builder()
                .imagePostRequestDto(imagePostRequestDto)
                .classroom(classroom)
                .build();
    }

    public void update(ImagePostRequestDto imagePostRequestDto) {
        this.title = imagePostRequestDto.getTitle();
    }
}
