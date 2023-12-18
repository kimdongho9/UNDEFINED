package com.lec.spring.service.community;

import com.lec.spring.domain.community.Feed;

public interface TagService {

    // feed 추가 시 tag add 하는 거
    int addTagForFeed(Feed feed);
}
