package com.lec.spring.repository;

import com.lec.spring.domain.user.User;
import com.lec.spring.repository.user.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class AuthorityRepositoryTest {

    @Autowired
    private SqlSession sqlSession;

    @Test
    void test1() {
        UserRepository userRepository = sqlSession.getMapper(UserRepository.class);
        User user = userRepository.findById(1L);
        AuthorityRepository authority = sqlSession.getMapper(AuthorityRepository.class);
        System.out.println("findById() : " + user);
        List<Authority> list = authority.findByUser(user);
        System.out.println("권한들 : " + list);

        user = userRepository.findByUsername("ADMIN1");
        System.out.println("findByUsername(): " + user);
        list = authority.findByUser(user);
        System.out.println("권한들 : " + list);
    }

    @Test
    void findByUser() {
    }

    @Test
    void addAuthority() {
    }
}