package com.example.jpa.domain.post.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

}
