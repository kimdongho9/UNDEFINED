package com.lec.spring.repository.community;

import com.lec.spring.domain.community.Feed;

import java.util.List;
import java.util.Set;

public interface FeedRepository {
    Feed findFeedById(Long id);
    List<String> findTagsByFeedId(Long feedId);
    int saveFeed(Feed feed);
    int deleteFeedById(Long feedId);
    Set<Long> feedIdListByNickname(String keyword);
    int updateFeed(Feed feed);
    List<Feed> findAllTempFeedByUserId(Long userId);


    
    // 상황에 따라 피드 글 수 반환 함수
    Long countAll();
    Long countAllByNickname(String nickname);
    Long countAllByTag(Set<Long> list);
    Long countFeedByUserId(Long userId, String state);


    // 상황에 따라 해당 페이지에 피드를 반환 하는 함수
    List<Feed> findAllCompFeedFromRow(Integer fromRow, Integer pageRows);
    List<Feed> listByNicknameFromRow(String nickname, Integer fromRow, Integer pageRows);
    List<Feed> listByTagFromRow(Set<Long> feedIdList, Integer fromRow, Integer pageRows);
    List<Feed> listByAllFromRow(Set<Long> feedIdList, Integer fromRow, Integer pageRows);

    List<Feed> myFeedFromRow(Long userId, Integer fromRow, Integer pageRows, String state);

    // 기타 동작을 위한 메소드
    List<Feed> findAllCompFeedByUserId(Long userId);
    List<Feed> findDelFeedByUserId(Long userId, String state);
}
