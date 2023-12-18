package com.lec.spring.domain.community;

import com.lec.spring.domain.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed {
    private Long feedId;
    private Long userId;
    private String feedTitle;
    private String feedContent;
    private String feedState;
    private Date feedRegdate;

    // 글의 추가적인 정보 <- 다른 테이블로 부터 얻어와야 함
    private int likeCnt;
    private String tagList;
    private String shortContent;

    private User user; // 글 작성자
    private List<Long> likeUserList;

    // 첨부파일
    @ToString.Exclude
    @Builder.Default
    private List<Photo> fileList = new ArrayList<>();
}
