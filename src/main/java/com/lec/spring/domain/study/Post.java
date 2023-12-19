package com.lec.spring.domain.study;

import com.lec.spring.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// DTO : Data Transfer Object
// 각각의 변수는 프로퍼티라고 부른다.
// 어떠한 데이터를 자바의 오브젝트로 변환시키기 위한 객체
public class Post {
    private Long id;  //setId();

    private String title;

    private String content;
    private Long viewCnt;
    private LocalDateTime regDate;
    private LocalDateTime startdate;
    private LocalDateTime enddate;
    private int member;
    private String position;


    private User user; //글 작성자(FK)

    private List<Skill> skillList;

    @ToString.Exclude
    String skills;
    @ToString.Exclude
    private String dataForEnddate;
    @ToString.Exclude
    private String dataForStartdate;


};
