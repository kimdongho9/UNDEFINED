package com.lec.spring.repository.naverapi;

import com.lec.spring.domain.naverapi.News;
import com.lec.spring.domain.naverapi.YoutubeDTO;

import java.util.List;

public interface NewsRepository {

    int save(News news);

    int saveYoutue(YoutubeDTO youtube);

    int delete(String keyword);

    List<News> list(String keyword);

    List<YoutubeDTO> listYoutube(String keyword);
}
