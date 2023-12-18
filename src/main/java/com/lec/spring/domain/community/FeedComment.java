package com.lec.spring.domain.community;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lec.spring.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedComment {
    private Long commentId;

    @JsonIgnore
    private Long commentFeedId;

    private Long parentId;
    private String commentContent;

    private List<FeedComment> replyList;

    @ToString.Exclude
    private User user;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm", timezone = "Asis/Seoul")
    @JsonProperty("regDate")
    private LocalDateTime regDate;
}
