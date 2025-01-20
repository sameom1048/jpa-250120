package com.example.jpa.domain.post.comment.repository;

import com.example.jpa.domain.post.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {}