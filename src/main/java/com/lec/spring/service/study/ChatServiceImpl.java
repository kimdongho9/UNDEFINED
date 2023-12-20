package com.lec.spring.service.study;


import com.lec.spring.domain.study.Chat;
import com.lec.spring.repository.study.ChatRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;
    // ChatRepository 의존성 주입을 위한 필드 선언

    // 생성자를 통한 ChatRepository 주입
    public ChatServiceImpl(SqlSession sqlSession){
        chatRepository = sqlSession.getMapper(ChatRepository.class);
        // SqlSession을 통해 ChatRepository 구현체를 가져와 의존성 주입
        System.out.println("짜잔"); // 생성자 호출 시 출력문
    }

    @Override
    public int saveChat(Chat chat) {
//        System.out.println("Service : " + chat.getContent());
        return chatRepository.save(chat); // 주어진 채팅 정보를 저장하는 메서드
    }

    @Override
    public List<Chat> findChatByPostId(int id) {
        return chatRepository.findAll(id); // 모든 채팅 목록을 조회하는 메서드
    }
}
