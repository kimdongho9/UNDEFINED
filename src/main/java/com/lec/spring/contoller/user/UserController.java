package com.lec.spring.contoller.user;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.user.User;
import com.lec.spring.domain.user.UserValidator;
import com.lec.spring.repository.user.ChatbotRepository;
import com.lec.spring.service.chatbot.ChatbotService;
import com.lec.spring.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.catalina.mapper.MappingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    /*@InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(userValidator);
    }*/

}
