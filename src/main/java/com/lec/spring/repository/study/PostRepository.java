package com.lec.spring.repository.study;

import com.lec.spring.domain.study.Post;
import com.lec.spring.domain.study.Skill;

import java.util.List;

public interface PostRepository extends SearchRepository {
    int save(Post post);
    Post findById(Long id);
    //조회수
    int incViewCnt (Long id);

    //목록 최신순
    List<Post> findAll();

    int update(Post post);

    int delete(Post post);

    List<Skill> getAllSkills();

    List<Skill> getSkillsByPostId(Long id);





}
