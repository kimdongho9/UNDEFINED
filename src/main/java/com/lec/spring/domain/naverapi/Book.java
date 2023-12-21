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
public class Book {
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private String link;
    private String image;
    private String description;
    private String publisher;
    private String isbn;
    private int discount;
    private String pubdate;
}
