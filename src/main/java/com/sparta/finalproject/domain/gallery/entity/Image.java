package com.sparta.finalproject.domain.gallery.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    private ImagePost imagePost;

    @Builder
    private Image(ImagePost imagePost, String imageUrl){
        this.imageUrl = imageUrl;
        this.imagePost = imagePost;
    }

    public static Image of(ImagePost imagePost, String imageUrl) {
        return Image.builder()
                .imagePost(imagePost)
                .imageUrl(imageUrl)
                .build();
    }
}
