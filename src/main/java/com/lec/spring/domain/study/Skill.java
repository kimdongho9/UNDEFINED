package com.lec.spring.domain.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {  //다중 선택 언어
    private Long postid;
    private Long skillId;
    private String skillName;
    private String imageUrl;  //fontawsome
}
