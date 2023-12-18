package com.lec.spring.domain.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {
    private Long id;
    private Long feedId; // 어느글의 첨부파일이누

    private String sourcename;
    private String filename;

    private boolean isImage;
}
