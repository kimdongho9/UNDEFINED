package com.lec.spring.config;


import com.lec.spring.controller.study.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket // WebSocket을 활성화하기 위한 어노테이션
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler; // ChatHandler를 주입받음

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // ChatHandler를 WebSocketHandler로 등록하고, "/ws/chat" 주소에 대한 핸들러를 추가함
        // 허용된 오리진은 모든 곳에서("*") 접근이 가능하도록 설정함
        registry.addHandler(chatHandler, "ws/chat")
                .setAllowedOrigins("http://ec2-43-203-27-225.ap-northeast-2.compute.amazonaws.com:8080");
    }
}
