package com.example.jpa.domain.post.post.sevice;

import com.example.jpa.domain.post.post.entity.Post;
import com.example.jpa.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post write(String title, String body) {
        Post post = new Post();

        post.setCreatedDate(LocalDateTime.now());
        post.setModifiedDate(LocalDateTime.now());
        post.setTitle(title);
        post.setBody(body);

        postRepository.save(post);

        return post;
    }
}
