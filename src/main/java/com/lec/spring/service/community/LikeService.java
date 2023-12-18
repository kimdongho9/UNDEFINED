package com.lec.spring.service.community;

public interface LikeService {
    int addLike(Long userId, Long feedId);

    int removeLike(Long userId, Long feedId);

    int countLike(Long feedId);
}
