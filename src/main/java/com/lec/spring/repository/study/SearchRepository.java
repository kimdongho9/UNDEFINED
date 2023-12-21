package com.lec.spring.repository.study;

import com.lec.spring.domain.study.Post;

import java.util.List;

public interface SearchRepository {
    List<Post> selectFromRow(Integer fromRow, Integer pageRows, String keyword, List<Long> postList);
    int countAll(String keyword, List<Long> postList);
}