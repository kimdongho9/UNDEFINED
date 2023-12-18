package com.lec.spring.repository.community;

import com.lec.spring.domain.community.Photo;

import java.util.List;

public interface PhotoRepository {
    public int save(Photo photo);

    List<Photo> findByFeed(Long feedId);

    Photo findById(Long fileId);

    int delete(Photo file);
}
