package com.lec.spring.service.chatbot;

import com.lec.spring.domain.chatbot.ChatbotMessage;

import java.util.List;

public interface ChatbotService {

    int add(ChatbotMessage msg);

    List<ChatbotMessage> loadByUserId(Long id);
}
