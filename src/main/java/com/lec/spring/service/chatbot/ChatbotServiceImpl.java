package com.lec.spring.service.chatbot;

import com.lec.spring.domain.chatbot.ChatbotMessage;
import com.lec.spring.repository.user.ChatbotRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatbotServiceImpl implements ChatbotService
{
    private ChatbotRepository chatbotRepository;

    public ChatbotServiceImpl(SqlSession sqlSession){
        chatbotRepository = sqlSession.getMapper(ChatbotRepository.class);
    }


    @Override
    public int add(ChatbotMessage msg) {

        chatbotRepository.save(msg);
        return 1;
    }
    public List<ChatbotMessage> loadByUserId(Long id){
        return chatbotRepository.loadByUserId(id);
    }
}
