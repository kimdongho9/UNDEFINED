package com.lec.spring.repository.portfolio;

import com.lec.spring.domain.portfolio.Portfolio;

import java.util.List;

public interface PortfolioRepository {

    // 포트폴리오를 저장하는 메서드
    int save(Portfolio portfolio);

    // 포스트의 조회수를 증가시키는 메서드
    int increaseViewCount(int post_id);

    // 모든 포트폴리오 목록을 조회하는 메서드
    List<Portfolio> findAll();

    // 포트폴리오 정보를 업데이트하는 메서드
    int update(Portfolio portfolio);

    // 포트폴리오를 삭제하는 메서드
    int delete(Portfolio portfolio);

    // 포트폴리오를 작성하는 메서드
    int write(Portfolio portfolio);

    Portfolio detail(Long id);

    // pagination
    List<Portfolio> selectFromRow(int from, int rows);

    int countAll();

}
