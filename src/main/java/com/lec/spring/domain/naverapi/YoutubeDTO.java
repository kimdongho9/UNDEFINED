package com.lec.spring.domain.naverapi;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YoutubeDTO {
    private int id;
    private String keyword;
    private String title;
    private String videoId;
}
