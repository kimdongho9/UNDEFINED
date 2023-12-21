package com.lec.spring.service.study;

import com.lec.spring.domain.study.Chat;

import java.util.List;

// 채팅 관련 데이터를 저장하고 조회하는 기능
public interface ChatService {

    // Chat 객체를 저장하는 메서드    save되면 1
    int saveChat(Chat chat);
    // saveChat: 채팅 데이터를 데이터베이스에 저장하는 메서드입니다. 반환값은 저장 성공 여부를 나타내는 정수입니다.

    // 특정 ID에 해당하는 Chat 객체를 조회하는 메서드
    List<Chat> findChatByPostId(int id);
    // findChatByPostId: 특정 ID에 해당하는 채팅 데이터를 조회하는 메서드입니다.
    // id 매개변수를 받아 해당 ID에 대한 채팅 리스트를 반환합니다.
}



//    // Chat 객체를 업데이트하는 메서드
//    int updateChat(Chat chat);
//
//    // Chat 객체를 삭제하는 메서드
//    int deleteChat(Chat chat);


//    // 모든 Chat 객체들을 조회하는 메서드
//    List<Chat> findAllChats();
