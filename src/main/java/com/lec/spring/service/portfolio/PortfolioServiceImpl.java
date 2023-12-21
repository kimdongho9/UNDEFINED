package com.lec.spring.service.portfolio;

import com.lec.spring.domain.portfolio.Portfolio;
import com.lec.spring.repository.portfolio.PortfolioRepository;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Value("10")
    private int PAGE_ROWS;

    @Value("10")
    private int WRITE_PAGES;

    private PortfolioRepository portfolioRepository;

    // SqlSession을 주입받아 PortfolioRepository를 구현한 클래스를 사용할 수 있도록 설정
    public PortfolioServiceImpl(SqlSession sqlSession){
        portfolioRepository = sqlSession.getMapper(PortfolioRepository.class);
        System.out.println("츄르 Churr"); // 간단한 출력문
    }

    // PortfolioRepository를 통해 신규 Portfolio를 저장하고 결과를 반환
    @Override
    public int save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    // PortfolioRepository를 통해 해당 포스트의 조회수를 증가시키고 결과를 반환
    @Override
//    @Transactional
    public int increaseViewCount(int postId) {
        return portfolioRepository.increaseViewCount(postId);
    }

    // PortfolioRepository를 통해 모든 Portfolio 목록을 가져와 반환
    @Override
    public List<Portfolio> findAll(Integer page, Model model) {
        // 현재 페이지 parameter
        if(page == null) page = 1;  // 디폴트는 1page
        if(page < 1) page = 1;

        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지가 표시되나
        // pageRows: 한 '페이지'에 몇개의 글을 리스트 할것인가?
        HttpSession session = U.getSession();
        Integer writePages = (Integer)session.getAttribute("writePages");
        if(writePages == null) writePages = WRITE_PAGES;  // 만약 session 에 없으면 기본값으로 동작
        Integer pageRows = (Integer)session.getAttribute("pageRows");
        if(pageRows == null) pageRows = PAGE_ROWS;  // 만약 session 에 없으면 기본값으로 동작

        // 현재 페이지 번호 -> session 에 저장
        session.setAttribute("page", page);

        long cnt = portfolioRepository.countAll();   // 글 목록 전체의 개수
        int totalPage = (int)Math.ceil(cnt / (double)pageRows);   // 총 몇 '페이지' ?

        // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지'
        int startPage = 0;
        int endPage = 0;

        // 해당 페이지의 글 목록
        List<Portfolio> list = null;

        if(cnt > 0){  // 데이터가 최소 1개 이상 있는 경우만 페이징
            //  page 값 보정
            if(page > totalPage) page = totalPage;

            // 몇번째 데이터부터 fromRow
            int fromRow = (page - 1) * pageRows;

            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            // 해당페이지의 글 목록 읽어오기
            list = portfolioRepository.selectFromRow(fromRow, pageRows);
            model.addAttribute("list", list);
        } else {
            page = 0;
        }

        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수

        // [페이징]
        model.addAttribute("url", U.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지

        return list;
    }

    // PortfolioRepository를 통해 Portfolio 정보를 업데이트하고 결과를 반환
    @Override
    public int update(Portfolio portfolio) {
        return portfolioRepository.update(portfolio);
    }

    // PortfolioRepository를 통해 Portfolio를 삭제하고 결과를 반환
    @Override
    public int delete(Portfolio portfolio) {
        return portfolioRepository.delete(portfolio);
    }

    // PortfolioRepository를 통해 Portfolio를 작성하고 결과를 반환
    @Override
    public int write(Portfolio portfolio) {
        return portfolioRepository.write(portfolio);
    }

    @Override
    public Portfolio detail(Long id) {
        return portfolioRepository.detail(id);
    }

    public List<Portfolio> forMainPage(){return portfolioRepository.forMainpage();}

    @Override
    public List<Portfolio> findByUserId(Long id) {
        return portfolioRepository.findByUserId(id);
    }
}
