package com.lec.spring.service.portfolio;



import com.lec.spring.domain.portfolio.Portfolio;
import org.springframework.ui.Model;

import java.util.List;

public interface PortfolioService {

    // Portfolio를 저장하는 메서드
    int save(Portfolio portfolio);

    // 해당 포스트의 조회수를 증가시키는 메서드
    int increaseViewCount(int postId);

    // 모든 Portfolio 목록을 조회하는 메서드
    List<Portfolio> findAll(Integer page, Model model);

    // Portfolio 정보를 업데이트하는 메서드
    int update(Portfolio portfolio);

    // Portfolio를 삭제하는 메서드
    int delete(Portfolio portfolio);

    // Portfolio를 작성하는 메서드
    int write(Portfolio portfolio);


    Portfolio detail(Long id);

    List<Portfolio> forMainPage();

}
