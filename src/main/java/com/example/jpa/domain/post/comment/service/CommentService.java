package com.example.jpa.domain.post.comment.service;

import com.example.jpa.domain.post.comment.entity.Comment;
import com.example.jpa.domain.post.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment write(long postId, String body) {

        Comment comment = Comment.builder()
                .postId(postId)
                .body(body)
                .build();

        return commentRepository.save(comment);
    }

}