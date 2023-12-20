package com.lec.spring.domain.study;

// lombok 라이브러리에서 제공하는 애노테이션들을 import

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Getter, Setter, equals, hashCode, toString 등을 자동으로 생성하는 Lombok 애노테이션
@NoArgsConstructor // 파라미터가 없는 생성자를 자동으로 생성하는 Lombok 애노테이션
@AllArgsConstructor // 모든 필드를 가지는 생성자를 자동으로 생성하는 Lombok 애노테이션
@Builder // 빌더 패턴을 사용할 수 있도록 함

// DTO : Data Transfer Object
// 어떠한 데이터를 자바의 오브젝트로 변환시키기 위한 객체
public class Chat {
    private int postId; // 채팅의 고유한 ID
    private int userId; // 사용자의 ID
    private String username; // 사용자 이름
    private String content; // 채팅 내용
    private LocalDateTime regDate; // 채팅이 등록된 시간

}
