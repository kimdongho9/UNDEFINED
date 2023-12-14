package com.lec.spring.contoller.chatbot;


import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.chatbot.ChatbotMessage;
import com.lec.spring.domain.chatbot.ChatbotRequest;
import com.lec.spring.domain.chatbot.ChatbotResponse;
import com.lec.spring.service.chatbot.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/bot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api-url}")
    private String apiUrl;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt, @AuthenticationPrincipal PrincipalDetails userDetails) {
        Long id = (userDetails != null) ? userDetails.getUser().getId() : null;
        ChatbotMessage newChatMessage = ChatbotMessage
                .builder()
                .user_id(id)
                .context(prompt)
                .isFromChat(0)
                .build();
        chatbotService.add(newChatMessage);
        // create a request
        ChatbotRequest request = new ChatbotRequest(model, prompt);

        // call the API
        ChatbotResponse response = restTemplate.postForObject(apiUrl, request, ChatbotResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }

        newChatMessage = ChatbotMessage
                .builder()
                .user_id(id)
                .context(response.getChoices().get(0).getMessage().getContent())
                .isFromChat(1)
                .build();
        chatbotService.add(newChatMessage);

        // return the first response
        return response.getChoices().get(0).getMessage().getContent();
    }

    @GetMapping("/loadchat")
    public List<ChatbotMessage> loadChat(@AuthenticationPrincipal PrincipalDetails userDetails){
        Long id = (userDetails != null) ? userDetails.getUser().getId() : null;
        return chatbotService.loadByUserId(id);
    }
}
