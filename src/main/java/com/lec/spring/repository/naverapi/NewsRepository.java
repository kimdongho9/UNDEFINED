package com.lec.spring.repository.naverapi;

import com.lec.spring.domain.naverapi.News;

import java.util.List;

public interface NewsRepository {

    int save(News news);

    int delete(String keyword);

    List<News> list(String keyword);
}
