package com.lec.spring.domain.chatbot;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;



@Data
public class ChatbotRequest {

    private String model;
    private List<Message> messages;
    private double temperature;




    public ChatbotRequest(String model, String prompt) {
        this.model = model;

        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

    // getters and setters
}
