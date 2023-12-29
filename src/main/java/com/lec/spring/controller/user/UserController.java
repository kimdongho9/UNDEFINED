package com.lec.spring.controller.user;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.calendar.Api;
import com.lec.spring.domain.naverapi.Book;
import com.lec.spring.domain.user.User;
import com.lec.spring.service.calendar.ApiService;
import com.lec.spring.service.naverapi.NaverApiService;
import com.lec.spring.service.portfolio.PortfolioService;
import com.lec.spring.service.study.StudyService;
import com.lec.spring.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private NaverApiService naverApiService;
    private StudyService studyService;
    private PortfolioService portfolioService;

    @Value("${zeomeeting.appID}")
    private int appID;

    @Value("${zeomeeting.serverSecret}")
    private String serverSecret;

    @Autowired
    public UserController(
            UserService userService,
            NaverApiService naverApiService,
            StudyService studyService,
            PortfolioService portfolioService
    ) {
        this.userService = userService;
        this.naverApiService = naverApiService;
        this.studyService = studyService;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/login")
    public String login(){return "user/login";};


    @PostMapping("/loginError")
    @ResponseBody
    public String loginError(){
        return "Failed";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String registerOk(User user){
        userService.register(user);
        return "user/login";
    }

    @GetMapping("/videocall")
    public String videocall(Model model, @AuthenticationPrincipal PrincipalDetails userDetails){
        if(userDetails != null){
            model.addAttribute("user", userDetails.getUser());
            model.addAttribute("appID", appID);
            model.addAttribute("serverSecret", serverSecret);
        }else{
            return "user/login";
        }

        return "user/videocall";
    }
    @GetMapping ("/isDuplicateUsername")
    @ResponseBody
    public int isDuplicateUsername(String id){
        return userService.isExist(id) ? 1 : 0;
    }
    @GetMapping("/isDuplicateName")
    @ResponseBody
    public int isDuplicateName(String name){
        return userService.isExistName(name) ? 1 : 0;
    }

    @PostMapping("/updateInfo")
    public String updateInfo(String username, String name, String password, String email, @AuthenticationPrincipal PrincipalDetails userDetails){
        User user = userDetails.getUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setUsername(username);
        userService.update(user);
        return "redirect:/";
    }


    @Autowired
    private ApiService apiService;

    @GetMapping("/calendar")
    public String Calendar(Model model){
        List<Api> list = apiService.list();
//        System.out.println("list : " + list);
        model.addAttribute("list", list);

        //index0 => 0번째 있는 데이터
        return "user/calendar";
    }

    @GetMapping("/books")
    public void books(){}

    @PostMapping("/booklikes")
    @ResponseBody
    public void booklikes(Book book, @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println(book);
        book.setUserId(userDetails.getUser().getId());
        naverApiService.saveBook(book);
    }

    @PostMapping("/bookunlikes")
    @ResponseBody
    public void bookunlikes(Book book, @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println(book);
        book.setUserId(userDetails.getUser().getId());
        naverApiService.deleteBook(book);
    }

    @GetMapping("/mypage")
    public String mypage(Model model,  @AuthenticationPrincipal PrincipalDetails userDetails){
        if(userDetails == null) return "user/login";
        Long id = userDetails.getUser().getId();
        model.addAttribute("bookList", naverApiService.likeBooks(id));    //이 유저가 좋아요 누른 책 정보
        model.addAttribute("studyList", studyService.listForMyPage(id));
        model.addAttribute("pfList", portfolioService.findByUserId(id));
        return "user/mypage";
    }

}
