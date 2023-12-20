package com.lec.spring.controller.study;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler {

    // 클라이언트 WebSocket 세션을 저장하기 위한 리스트
    private static List<WebSocketSession> list = new ArrayList<>();

    // 클라이언트로부터 메시지를 수신하고 처리하는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
       // 클라이언트로부터 메시지를 수신하면 해당 메시지를 로그에 기록하고 연결된 모든 클라이언트에게 전송
       // log (로그) = 사이트에 접속해 활동한 기록
        String payload = message.getPayload();
        log.info("payload : " + payload);

        // 모든 연결된 클라이언트에게 메시지 전송
        for (WebSocketSession sess : list) {
            sess.sendMessage(message);
        }
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트가 연결될 때마다 해당 세션을 리스트에 추가하고 이를 로그에 기록
        // log (로그) = 사이트에 접속해 활동한 기록
        list.add(session); // 클라이언트 세션을 리스트에 추가
        log.info(session + " 클라이언트 접속");
    }

    /* Client가 접속 해제 시 호출되는 메서드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 클라이언트 연결이 종료될 때 해당 세션을 리스트에서 제거하고 이를 로그에 기록
        // log (로그) = 사이트에 접속해 활동한 기록
        list.remove(session); // 클라이언트 세션을 리스트에서 제거
        log.info(session + " 클라이언트 접속 해제");
    }
}
