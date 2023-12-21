package com.lec.spring.domain.portfolio;


import com.lec.spring.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


// DTO : Data Transfer Object
// 어떠한 데이터를 자바의 오브젝트로 변환시키기 위한 객체
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    private int userId;
    private int postId;
    // 네임 추가 할듯 (열정 제인이 요청함)
    private User user;
    private String content;
    private String title;
    private String experience; // 경력
    private String userPr; // 자기소개 (한문장으로 표현 할만한?)
    private int increaseViewCount;   // 조회수
    private LocalDateTime regDate;
}

