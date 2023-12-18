package com.lec.spring.repository.community;

import java.util.List;

public interface LikeRepository {
    int addLike(Long userId, Long feedId);

    int removeLike(Long userId, Long feedId);

    int countLike(Long feedId);

    List<Long> findUsers(Long feedId);
}
