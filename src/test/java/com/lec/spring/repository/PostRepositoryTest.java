package com.lec.spring.repository;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest   //  스프링 context 를 로딩하여 테스트
class PostRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    void test1(){
        PostRepository postRepository = sqlSession.getMapper(PostRepository.class);

        Post post = Post.builder()
                //.user("허지우")
                .subject("우와 퇴근이다")
                .content("오늘도 칼퇴!")
                .build();
        System.out.println("save() 전: " + post);
        int cnt = postRepository.save(post);
        System.out.println("save() 후: " + post);
    }

    @Test
    void test2(){
        PostRepository postRepository = sqlSession.getMapper(PostRepository.class);

        Long id = 1L;
        postRepository.incViewCnt(id);
        Post post = postRepository.findById(id);
        System.out.println("findById() 결과: " + post);
    }

    @Test
    void test3(){
        PostRepository postRepository = sqlSession.getMapper(PostRepository.class);

        List<Post> list = postRepository.findAll();
        list.forEach(System.out::println);
    }

    @Test
    void test4(){
        PostRepository postRepository = sqlSession.getMapper(PostRepository.class);
        Post post = Post.builder()
                .id(1L)
                .subject("집에가고 싶어요")
                .content("이런 경험 처음이야..")
                .build();

        System.out.println("수정전: " + postRepository.findById(1L));
        int cnt = postRepository.update(post);
        System.out.println("수정후: " + postRepository.findById(1L));
        
    }

    @Test
    void test5(){
        PostRepository postRepository = sqlSession.getMapper(PostRepository.class);
        Post post = new Post();
        post.setId(5L);

        System.out.println("삭제전: " + postRepository.findAll());
        postRepository.delete(post);
        System.out.println("삭제후: " + postRepository.findAll());
    }
}




