package com.example.jpa.domain.post.post.sevice;

import com.example.jpa.domain.post.post.entity.Post;
import com.example.jpa.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post write(String title, String body) {

        Post post = Post.builder()
                .title(title)
                .body(body)
                .build();



        postRepository.save(post);

        return post;
    }

    public Post modify(Post post, String title, String body) {
        post.setTitle(title);
        post.setBody(body);

        postRepository.save(post);
        return post;

    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public long count() {
        return postRepository.count();
    }
}
