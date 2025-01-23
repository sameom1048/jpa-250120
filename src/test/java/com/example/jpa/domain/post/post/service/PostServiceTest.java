package com.example.jpa.domain.post.post.service;

import com.example.jpa.domain.member.entity.Member;
import com.example.jpa.domain.member.service.MemberService;
import com.example.jpa.domain.post.post.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("글 2개 작성")
    @Transactional
    public void t1() {

        Member user1 = memberService.findByUsername("user1").get();

        postService.write(user1, "title1", "body1");
        postService.write(user1, "title2", "body2");
    }

    @Test
    @DisplayName("모든 글 조회")
    @Transactional
    public void t2() {

        List<Post> all = this.postService.findAll();
        assertThat(all.size()).isEqualTo(3);

        Post q = all.get(0);
        assertThat("title1").isEqualTo(q.getTitle());

    }

    @Test
    @DisplayName("아이디로 글 조회")
    @Transactional
    public void t3() {

        Optional<Post> opPost = postService.findById(1);

        if(opPost.isPresent()) {
            assertThat(opPost.get().getTitle()).isEqualTo("title1");
        }

    }

    @Test
    @DisplayName("제목으로 검색")
    @Transactional
    public void t4() {

        List<Post> posts = postService.findByTitle("title1"); // select * from post where title = 'title1';

        assertThat(posts.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("제목과 내용으로 글 조회")
    @Transactional
    public void t5() {

        // SELECT * FROM post WHERE title = ? and body = ?;

        List<Post> posts = postService.findByTitleAndBody("title1", "body1");
        assertThat(posts.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("제목이 포함된 결과 조회")
    @Transactional
    public void t6() {

        // SELECT * FROM post WHERE title LIKE ?;

        List<Post> posts = postService.findByTitleLike("title%");
        assertThat(posts.size()).isEqualTo(3);

    }


    @Test
    @DisplayName("아이디 순으로 내림차순 정렬")
    @Transactional
    public void t7() {

        // SELECT * FROM post ORDER BY id DESC;

        List<Post> posts = postService.findByOrderByIdDesc();
        assertThat(posts.size()).isEqualTo(3);

        assertThat(posts.get(0).getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("위에서 2개만 조회")
    @Transactional
    public void t8() {

        // SELECT * FROM post where title = ? ORDER BY id DESC LIMIT 2;

        List<Post> posts = postService.findTop2ByTitleOrderByIdDesc("title1");
        assertThat(posts.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("findAll(Pageable pageable)")
    void t11() {

        // SELECT * FROM post LIMIT 2, 2;

        // 현재 페이지, 한 페이지에 보여줄 아이템

        int itemsPerPage = 2; // 한 페이지에 보여줄 아이템 수
        int pageNumber = 2; // 현재 페이지 == 2
        pageNumber--; // 1을 빼는 이유는 jpa는 페이지 번호를 0부터 시작하기 때문
        Pageable pageable = PageRequest.of(pageNumber, itemsPerPage, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> postPage = postService.findAll(pageable);
        List<Post> posts = postPage.getContent();

        assertEquals(1, posts.size()); // 글이 총 3개이고, 현재 페이지는 2이므로 1개만 보여야 함
        Post post = posts.get(0);
        assertEquals(1, post.getId());
        assertEquals("title1", post.getTitle());
        assertEquals(3, postPage.getTotalElements()); // 전체 글 수
        assertEquals(2, postPage.getTotalPages()); // 전체 페이지 수
        assertEquals(1, postPage.getNumberOfElements()); // 현재 페이지에 노출된 글 수
        assertEquals(pageNumber, postPage.getNumber()); // 현재 페이지 번호
    }

    @Test
    @DisplayName("findByTitleLike(Pageable pageable)")
    void t12() {

        // SELECT * FROM post WHERE title LIKE 'title%' ORDER BY id DESC LIMIT 0, 10;

        // 현재 페이지, 한 페이지에 보여줄 아이템

        int itemsPerPage = 10; // 한 페이지에 보여줄 아이템 수
        int pageNumber = 1; // 현재 페이지 == 2
        pageNumber--; // 1을 빼는 이유는 jpa는 페이지 번호를 0부터 시작하기 때문
        Pageable pageable = PageRequest.of(pageNumber, itemsPerPage, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> postPage = postService.findByTitleLike("title%", pageable);
        List<Post> posts = postPage.getContent();

        assertEquals(3, posts.size()); // 글이 총 3개이고, 현재 페이지는 2이므로 1개만 보여야 함
        Post post = posts.get(0);
        assertEquals(3, post.getId());
        assertEquals("title1", post.getTitle());
        assertEquals(3, postPage.getTotalElements()); // 전체 글 수
        assertEquals(1, postPage.getTotalPages()); // 전체 페이지 수
        assertEquals(3, postPage.getNumberOfElements()); // 현재 페이지에 노출된 글 수
        assertEquals(pageNumber, postPage.getNumber()); // 현재 페이지 번호
    }

    @Test
    @DisplayName("회원 정보로 글 조회")
    void t13() {

        // 회원 아이디로 회원이 작성한 글 목록 가져오기
        // SELECT * FROM post p left JOIN member m ON p.author_id = m.id where username = 'user1';

        // post에서 member 정보가 필요할 때 방법
        // 1. post를 먼저 조회해서 member id를 알아온 후 -> member 조회 -> select 2번 조회
        // 2. post랑 member를 붙여서 같이 조회 -> join

//        Member user1 = memberService.findByUsername("user1").get();
        List<Post> posts = postService.findByAuthorUsername("user1");

        assertThat(posts.size()).isEqualTo(1);

    }

    @Test
    @DisplayName("회원 정보로 글 조회2")
    @Transactional
    void t14() {

        // 회원 아이디로 회원이 작성한 글 목록 가져오기
        // SELECT * FROM post p WHERE INNER JOIN member m ON p.member_id = m.id where username = 'user1';

        // post에서 member 정보가 필요할 때 방법
        // 1. post를 먼저 조회해서 member id를 알아온 후 -> member 조회 -> select 2번 조회
        // 2. post랑 member를 붙여서 같이 조회 -> join

        // 대부분의 경우 JPA는 연관된 정보를 가져올 때 select를 여러번 날린다.

        List<Post> posts = postService.findByAuthorUsername("user1");
        Post post = posts.get(0);

        System.out.println(post.getId() + ", " + post.getTitle());
        System.out.println(post.getAuthor().getUsername());

    }

    @Test
    @DisplayName("글목록에서 회원 정보 가져오기 -> N + 1")
    @Transactional
    void t15() {

        // post에서 member 정보가 필요할 때 방법
        // 1. post를 먼저 조회해서 member id를 알아온 후 -> member 조회 -> select 2번 조회
        // 2. post랑 member를 붙여서 같이 조회 -> fetch join -> jpql
        // 3. select * from post where member_id = 1 -> 1번
        // 4. select * from post where member_id = 2 -> 2번
        // 5. select * from post where member_id = 3 -> 3번
        // ...
        // 100. select * from post where member_id = 100 -> 100번

        // select * from post where member_id in (1,2,3,4,5...,100);
        // select * from post where member_id in (?,?,?,?)


        List<Post> posts = postService.findAll(); // 목록 조회 sql 1

        // 목록의 개수 만큼 추가 select 발생 N
        for(Post post : posts) {
            System.out.println(post.getId() + ", " + post.getTitle() + ", " + post.getAuthor().getNickname());
        }

    }
}