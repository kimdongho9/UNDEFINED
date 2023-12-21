package com.lec.spring.service.study;

import com.lec.spring.domain.study.Favor;
import com.lec.spring.domain.study.Post;
import com.lec.spring.domain.study.Skill;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

public interface StudyService {


    int write(Post post, String skillNames);

    @Transactional
    Post detail(Long id);

    // 페이징 리스트
    List<Post> list(Integer page, Model model);
    List<Post> listByKeyword(Integer page, Model model, String keyword);
    List<Post> listBySkill(Integer page, Model model, String skill);
    List<Post> listByFavor(Integer page, Model model, String skill, String favor, Long userId);
    List<Post> favorByAll(Integer page, Model model, String skill, String favor, Long userId);

    //특정 id 의 글 읽어오기(SELECT)
    //조회수 증가 없음
    Post selectById(long id);


    //특정 id 글 수정하기(제목,내용) 업데이트
    int update(Post post, String skillNames);

    //특정 id 글 삭제하기(DELETE)
    int deleteById(Long id);


    List<Skill> getSkillsByPostId(Long id); //4
    List<Favor> getFavorByUserId(Long id);

    int Favsave(Long postid, Long userid);
    int Favdelete(Long postid, Long userid);

    List<Post> listForMyPage(Long id);
}

