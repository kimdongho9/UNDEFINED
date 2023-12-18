package com.lec.spring.repository.naverapi;

import com.lec.spring.domain.naverapi.News;

public interface NewsRepository {

    int save(News news);

    int delete(String keyword);
}
