package com.lec.spring.service.community;

import com.lec.spring.domain.community.Feed;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {

    int writeFeed(Feed feed, Long userId, List<MultipartFile> files);
    int deleteFeed(Long feedId);
    int updateFeed(Feed feed, List<MultipartFile> newFiles, Long[] deleteList);
    int trashFeed(Long feedId);
    Feed findFeedById(Long id);
    int trashFeedAllByUserId(Long userId);
    int deleteFeedAllByUserId(Long userId, String state);
    int restoreFeed(Long feedId);


    // 리스트 함수
    List<Feed> list(Integer page, Model model);
    List<Feed> listByNickname(String nickname, Integer page, Model model);
    List<Feed> listByTag(String nickname, Integer page, Model model);
    List<Feed> listByAll(String keyword, Integer page, Model model);
    void listByOption(String option, String keyword, Integer page, Model model);

    // User Id 별 리스트 함수
    List<Feed> listByUserId(Long userId, Integer page, Model model, String state);
}
