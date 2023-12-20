package com.lec.spring.controller.user;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.calendar.Api;
import com.lec.spring.domain.user.User;
import com.lec.spring.service.calendar.ApiService;
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
    @Autowired
    private UserService userService;

    @Value("${zeomeeting.appID}")
    private int appID;

    @Value("${zeomeeting.serverSecret}")
    private String serverSecret;
    @GetMapping("/login")
    public void login(){};

    //user/login.html
    // onAuthenticationFailure 에서 로그인 실패시 forwarding 용
    // request 에 담겨진 attribute 는 Thymeleaf 에서 그대로 표현 가능.
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


}
