package com.lec.spring.domain.naverapi;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {
    private Long id;
    private String keyword;
    private String title;
    private String originallink;
    private String link;
    private String description;
    private LocalDateTime pubDate;
}