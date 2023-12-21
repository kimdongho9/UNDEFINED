package com.lec.spring.repository.study;

import com.lec.spring.domain.study.Chat;

import java.util.List;

public interface ChatRepository { // ChatRepository 인터페이스 선언

    // 채팅 정보 저장
    int save(Chat chat);

    // 채팅 ID로 특정 채팅 조회
    Chat findById(int id);

    // 모든 채팅 목록 조회
    List<Chat> findAll(int post_id);

    // 채팅 정보 업데이트
    int update(Chat chat);

    // 채팅 삭제
    int delete(Chat chat);
}
