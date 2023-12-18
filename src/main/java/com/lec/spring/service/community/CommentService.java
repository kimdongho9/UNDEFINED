package com.lec.spring.service.community;

import com.lec.spring.domain.community.QryCommentList;
import com.lec.spring.domain.community.QryResult;

public interface CommentService {
    QryResult addCommentByFeedId(Long feedId, Long parentId, Long userId, String content);

    QryResult deleteCommentById(Long commentId);

    QryCommentList list(Long feedId);

}
