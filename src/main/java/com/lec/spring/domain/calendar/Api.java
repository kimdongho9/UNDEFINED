package com.lec.spring.domain.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Api {
    private String implplannm; // 회차
    private String jmfldnm; // 종목명
    private String fee; // 응시료
    private String docexamenddt; // 필기시험 종료일자
    private String docexamstartdt; // 필기시험 시작일자
    private String docpassdt; // 필기시험 합격(예정)자 발표 일정
    private String docregenddt; // 필기시험 원서접수 종료 일자
    private String docregstartdt; // 필기시험 원서 접수 시작 일자
    private String pracexamenddt; // 실기시험 종료 일자
    private String pracexamstartdt; // 실기시험 시작 일자
    private String pracpassenddt; // 합격자 발표 종료 일자
    private String pracpassstartdt; // 합격자 발표 시작 일자
    private String pracregenddt; // 실기시험 원서 접수 종료 일자
    private String pracregstartdt; // 실기시험 원서 접수 시작 일자
}
