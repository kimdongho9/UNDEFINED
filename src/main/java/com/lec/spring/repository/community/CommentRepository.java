package com.lec.spring.repository.community;

import com.lec.spring.domain.community.FeedComment;

import java.util.List;

public interface CommentRepository {

    // feed id 로 해당 피드의 댓글 찾기
    List<FeedComment> findCommentsByFeedId(Long id);

    // 댓글 작성
    int addComment(FeedComment feedComment);

    // 댓글 삭제
    int deleteComment(Long commentId);

    List<FeedComment> findRepliesByFeedId(Long commentId);


    // 댓글 수정?? => 기획에는 안 넣긴 했음
}
