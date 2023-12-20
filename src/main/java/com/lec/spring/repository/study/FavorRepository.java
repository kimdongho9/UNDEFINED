package com.lec.spring.repository.study;

import com.lec.spring.domain.study.Favor;

import java.util.List;

public interface FavorRepository {

    List<Favor> findByPostId(Long postid);

    List<Favor> findByUserId(Long userid);

    int save(Long postid, Long userid);
    int delete(Long postid, Long userid);

    Favor isFavor(Long postId, Long userId);
}
