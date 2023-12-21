package com.lec.spring.controller.user;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.naverapi.Book;
import com.lec.spring.domain.naverapi.News;
import com.lec.spring.domain.naverapi.YoutubeDTO;
import com.lec.spring.domain.user.User;
import com.lec.spring.service.naverapi.NaverApiService;
import com.lec.spring.service.portfolio.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private NaverApiService naverApiService;

    @Autowired
    private PortfolioService portfolioService;

    @RequestMapping("/")
    public String login(Model model){
        model.addAttribute("infoFirst", naverApiService.list("개발자채용").get(0));
        model.addAttribute("info", naverApiService.list("개발자채용").subList(1, 6));

        model.addAttribute("backFirst", naverApiService.list("백엔드").get(0));
        model.addAttribute("back", naverApiService.list("백엔드").subList(1, 6));

        model.addAttribute("frontFirst", naverApiService.list("프론트엔드").get(0));
        model.addAttribute("front", naverApiService.list("프론트엔드").subList(1, 6));

        model.addAttribute("AIFirst", naverApiService.list("AI").get(0));
        model.addAttribute("AI", naverApiService.list("AI").subList(1, 6));

        model.addAttribute("AI2First", naverApiService.list("인공지능").get(0));
        model.addAttribute("AI2", naverApiService.list("인공지능").subList(1, 6));

        model.addAttribute("portfolio", portfolioService.forMainPage());

        return "/home";
    }


    //-------------------------------------------------------------------------
    // 현재  Authentication 보기 (디버깅 등 용도로 활용)
    @RequestMapping("/auth")
    @ResponseBody
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 매개변수에 Authentication 을 명시해도 주입된다 (인증한 이후)
    @RequestMapping("/auth2")
    @ResponseBody
    public Authentication auth2(Authentication authentication){
        return authentication;
    }

    @RequestMapping("/userDetails")
    @ResponseBody
    public PrincipalDetails userDetails(Authentication authentication){
        return (PrincipalDetails)authentication.getPrincipal();
    }

    // @AuthenticationPrincipal 을 사용하여 로그인한 사용자 정보 주입받을수 있다.
    // org.springframework.security.core.annotation.AuthenticationPrincipal
    @RequestMapping("/user")
    @ResponseBody
    public User username(@AuthenticationPrincipal PrincipalDetails userDetails){
        return (userDetails != null) ? userDetails.getUser() : null;
    }

    @GetMapping("/naver/news")
    @ResponseBody
    public List<News> list(int start, String keyword){
        return naverApiService.list(keyword).subList(start, start+5);
    }

    @GetMapping("/youtube/list")
    @ResponseBody
    public List<YoutubeDTO> listYoutube(String keyword){
        System.out.println(keyword.trim());
        return naverApiService.listYoutube(keyword.trim());
    }

    @GetMapping("/naver/book")  //5개 for calendar
    @ResponseBody
    public List<Book> listbook(String keyword){
        return naverApiService.getBooksInCalendar(keyword.trim());
    }

    @GetMapping("/naver/books") //10개
    @ResponseBody
    public List<Book> listbooks(String keyword){
        return naverApiService.getbooks(keyword.trim());
    }

}












