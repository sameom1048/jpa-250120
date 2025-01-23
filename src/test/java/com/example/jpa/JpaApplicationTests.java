package com.example.jpa;

import com.example.jpa.domain.post.post.service.Post;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class JpaApplicationTests {

    @Test
    void t1() {

        Post p1 = new Post(1,"title1");
        Post p2 = new Post(1,"title1");

        assertThat(p1).isEqualTo(p2);

//        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());

    }

    @Test
    void t2() {

        Post p1 = new Post(1,"title1");
        Post p2 = new Post(1,"title1");

        Set<Post> posts = new HashSet<>();  // 중복 허용 X

        posts.add(p1);
        posts.add(p2);

        assertThat(posts.size()).isEqualTo(1);

    }

}
