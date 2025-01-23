package com.example.jpa.global;

import com.example.jpa.domain.member.entity.Member;
import com.example.jpa.domain.member.service.MemberService;
import com.example.jpa.domain.post.comment.entity.Comment;
import com.example.jpa.domain.post.comment.service.CommentService;
import com.example.jpa.domain.post.post.entity.Post;
import com.example.jpa.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final PostService postService;
    private final CommentService commentService;

    // 프록시 객체를 획득
    @Autowired
    @Lazy
    private BaseInitData self; // 프록시
    @Autowired
    private MemberService memberService;


    @Bean
    @Order(1)
    public ApplicationRunner applicationRunner() {
        return args -> {
            self.work1();
            self.work2();
        };
    }


    @Transactional
    public void work1() {

        if(memberService.count() > 0) {
            return;
        }

        // 회원 샘플데이터 생성
        memberService.join("system", "1234", "시스템");
        memberService.join("admin", "1234", "관리자");
        memberService.join("user1", "1234", "유저1");
        memberService.join("user2", "1234", "유저2");
        memberService.join("user3", "1234", "유저3");

    }

    @Transactional
    public void work2() {

        if (postService.count() > 0) {
            return;
        }

        Member user1 = memberService.findByUsername("user1").get();
        Member user2 = memberService.findByUsername("user2").get();
        Member user3 = memberService.findByUsername("user3").get();

        Post p1 = postService.write(user1, "title1", "body1");
        Post p2 = postService.write(user2, "title1", "body2");
        Post p3 = postService.write(user3, "title1", "body3");

        p1.addTag("JPA");
        p1.addTag("SpringBoot");
        p1.addTag("개발");
        p1.addTag("개발");

        Comment c1 = Comment.builder()
                .author(user1)
                .body("comment1")
                .build();

        p1.addComment(c1);

        Comment c2 = Comment.builder()
                .author(user1)
                .body("comment2")
                .build();

        p1.addComment(c2);

        Comment c3 = Comment.builder()
                .author(user2)
                .body("comment3")
                .build();

        p1.addComment(c3);

    }
}