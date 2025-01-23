package com.example.jpa.domain.post.comment.service;

import com.example.jpa.domain.member.entity.Member;
import com.example.jpa.domain.post.comment.entity.Comment;
import com.example.jpa.domain.post.comment.repository.CommentRepository;
import com.example.jpa.domain.post.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment write(Member author, Post post, String body) {

        Comment comment = Comment.builder()
                .author(author)
                .post(post)
                .body(body)
                .build();

        return commentRepository.save(comment);
    }

    public long count() {
        return commentRepository.count();
    }

    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment c1) {
        return commentRepository.save(c1);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}