package com.lec.spring.repository.study;

import com.lec.spring.domain.study.Skill;

import java.util.List;

public interface SkillRepository {


    Skill findByName(String skillName);

    int saveSkills(Long skillId, Long id);

    List<Skill> findSkillsByPostId(Long id);

    int delSkills(Long skillId, Long postId);


    List<Long> findSkillIdsByName(List<String> skillList);
}
