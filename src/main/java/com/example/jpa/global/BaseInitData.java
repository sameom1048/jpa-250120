package com.example.jpa.global;

import com.example.jpa.domain.post.post.entity.Post;
import com.example.jpa.domain.post.post.sevice.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final PostService postService;

    @Bean
    @Order(1)
    public ApplicationRunner applicationRunner() {
        return args -> {

            // 데이터가 3개가 이미 있으면 패스
            if( postService.count() > 0 ) {
                return ;
            }

            Post p1 = postService.write("title1", "body1");
            Post p2 = postService.write("title2", "body2");
            Post p3 = postService.write("title3", "body3");
            System.out.println("p1: " + p1.getId());
            System.out.println("p2: " + p2.getId());
            System.out.println("p3: " + p3.getId());
        };
    }

    @Bean
    @Order(2)
    public ApplicationRunner applicationRunner2() {

        return new ApplicationRunner() {
            @Override
            @Transactional
            public void run(ApplicationArguments args) throws Exception {
                Post post = postService.findById(1L).get();
                Thread.sleep(1000);
                postService.modify(post, "new title122", "new body122");
            }
        };
    }

    @Bean
    @Order(3)
    public ApplicationRunner applicationRunner3() {

        return new ApplicationRunner() {
            @Override
            @Transactional
            public void run(ApplicationArguments args) throws Exception {
                Post p1 = postService.findById(1L).get();
                Post p2 = postService.findById(2L).get();
                Thread.sleep(1000);

                System.out.println("------p1 delete start------");
                postService.delete(p1);
                System.out.println("-------p1 delete finish-----");

                System.out.println("-----p2 delete start--------");
                postService.delete(p2);
                System.out.println("------p2 delete finish-------");

            }
        };
    }
}
