package com.lec.spring.controller.study;

import com.lec.spring.domain.study.Chat;
import com.lec.spring.service.study.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// RESTful API Controller는 클라이언트의 요청을 받아들이고, 해당 요청에 따라 적절한 작업을 수행하여 데이터를 처리하고 응답을 반환

@RestController // 컨트롤러임을 나타내는 어노테이션
@RequestMapping("/rest") // 이 컨트롤러에서 처리하는 요청의 기본 URL 경로를 지정
public class ChatRTController {

    @Autowired // 의존성 주입을 위한 어노테이션
    private ChatService chatService; // ChatService 의존성 주입

    private static int count = 0; // 요청 횟수를 추적하는 정적 변수

    @PostMapping("/onOpen") // HTTP POST 요청에 대한 매핑
    public Integer onOpen() {
        return count++; // 요청이 들어올 때마다 요청 횟수를 증가시킨 후 반환
    }

    @PostMapping("/saveDB") // HTTP POST 요청에 대한 매핑
    public int saveDB(Chat chat){
         // 받은 채팅 데이터를 데이터베이스에 저장하는 서비스 호출
        System.out.println("Controller : " + chat.getUserId());
        return chatService.saveChat(chat); // 정수값 반환 (저장 성공 여부를 나타내는 일반적인 반환값)
    }
}
