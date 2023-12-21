//package com.lec.spring.contoller.portfolio;
//
//
//import com.lec.spring.domain.portfolio.Portfolio;
//import com.lec.spring.service.portfolio.PortfolioService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//// @RestController // 웹서비스의 엔드포인트를 처리하기 위해 사용
//// 해당 컨트롤러의 모든 메서드의 반환값이 HTTP 응답으로 자동으로 변환되어 JSON 또는 XML 형식의 데이터로 클라이언트에게 전송
//@Controller  // 해당 클래스를 Spring의 Controller로 등록
////@Log4j2  // Lombok을 사용하여 log(기록)을 남기기위한 어노테이션 추가
//@RequestMapping("/portfolio")  // 요청 매핑 경로 설정
//public class PortfolioController {
//
//
//    private final PortfolioService portfolioService;
//
//    // PortfolioService를 주입받는 생성자
//    public PortfolioController(PortfolioService portfolioService){
//        this.portfolioService = portfolioService;
//    }
//
//
//    @GetMapping("/list")  // HTTP GET 메서드로 "/study/port/list" 경로 요청을 처리
//    public String list(Model model){
//        List<Portfolio> list = portfolioService.findAll();  // 포트폴리오 목록을 Service를 통해 가져옴
//        model.addAttribute("List", list);// "portfolio"라는 이름으로 포트폴리오 목록을 Model에 추가
//
//        return "portfolio/list";// 실제로 포트폴리오 목록을 표시할 View의 이름을 반환
//
//    }
//
//
//
//
//    @GetMapping("/write")
//    public String write(Portfolio portfolio){
//        return "portfolio/write";
//        // write 페이지로 이동하는 메서드로서 추가 작업이 필요 없는 경우에는 따로 작성할 내용이 없습니다.
//    }
//
//    @PostMapping("/write")
//    public String write(Portfolio portfolio, Model model) {
//        int result = portfolioService.write(portfolio); // 포트폴리오 저장
//
//        if (result >= 1) { // 성공적으로 실행되면 1 이상의 값을 반환하므로 1 이상의 값이면 성공
//            return "redirect:/portfolio/list"; // 저장 성공 시 목록 페이지로 리다이렉트
//        } else {
//            // 실패 시 포트폴리오 객체와 실패 메시지를 모델에 추가하여 글쓰기 페이지로 이동
//            model.addAttribute("portfolio", portfolio);
//            model.addAttribute("error", "포트폴리오를 저장하는데 실패했습니다.");
//
//            return "portfolio/write"; // 실패 시 다시 글쓰기 페이지로 이동
//        }
//    }
//
//
//    @GetMapping("/detail/{id}")
//    public String detail(@PathVariable Long id, Model model){
//        model.addAttribute("portfolio", portfolioService.detail(id));
//        return "portfolio/detail";
//    }
//
//
//
//
//// -----------------------------------------------------------------------
////    // 모든 포트폴리오 목록을 조회하여 View로 전달하는 메서드
////    @GetMapping("/all")
////    public String getAllPortfolios(Model model) { <-모든 포폴을 조회후에 portfolio데이터에 전달
////        List<Portfolio> portfolios = portfolioService.findAll();
////        model.addAttribute("portfolios", portfolios);
////        return "portfolio-list"; // 해당하는 View의 이름을 반환
////    }
////
////    // 포트폴리오 작성 폼으로 이동하는 메서드
////    @GetMapping("/write-form")
////    public String showWriteForm() {
////        return "write-portfolio"; // 포트폴리오 작성 폼의 View 이름을 반환
////    }
////
////    // 포트폴리오 작성을 처리하는 메서드
////    @PostMapping("/write")
////    public String writePortfolio(@ModelAttribute("portfolio") Portfolio portfolio) {
////        portfolioService.write(portfolio);
////        return "redirect:/portfolio/all"; // 작성 후 목록으로 리다이렉트  <- 포폴작성후에 다시 리스트로 리다이렉트
////    }
////
////    // 기타 기능들에 대한 메서드들 추가 가능
////}
//
//
//
//}

package com.lec.spring.controller.portfolio;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.portfolio.Portfolio;
import com.lec.spring.domain.portfolio.PortfolioValidator;
import com.lec.spring.domain.user.User;
import com.lec.spring.service.portfolio.PortfolioService;
import com.lec.spring.util.U;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService){
        this.portfolioService = portfolioService;
    }

    @GetMapping("/list")  // HTTP GET 메서드로 "/study/port/list" 경로 요청을 처리
    public void list(Integer page, Model model){
//        List<Portfolio> list = portfolioService.findAll();  // 포트폴리오 목록을 Service를 통해 가져옴
//        model.addAttribute("list", list);// "portfolio"라는 이름으로 포트폴리오 목록을 Model에 추가

        portfolioService.findAll(page, model);
//        return "portfolio/list";// 실제로 포트폴리오 목록을 표시할 View의 이름을 반환

    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){
        Portfolio portfolio = portfolioService.detail(id);
        int result = portfolioService.delete(portfolio);
        model.addAttribute("result", result);
        return "portfolio/deleteOk";
    }

    @GetMapping("/write")
    public String write(){
        return "portfolio/write";
    }

    @PostMapping("/write")
    public String writeOk(
            @Valid Portfolio portfolio           // 유효성 검사를 위해 @Valid 어노테이션 사용하고, Coupon 객체를 받습니다.
            , BindingResult result         // 검증 결과를 담는 BindingResult 객체입니다.
            , Model model                  // 뷰로 데이터를 넘기기 위한 Model 객체입니다.
            , RedirectAttributes redirectAttrs  // 리다이렉트 시 데이터를 보내기 위한 RedirectAttributes 객체입니다.
    , @AuthenticationPrincipal PrincipalDetails userDetails // 현재 인증된 사용자의 세부 정보를 주입받기 위해 사용되는 어노테이션
    ){
        System.out.println("?? 호출되나??");

        if(result.hasErrors()){ // 검증 결과 에러가 있을 경우

            // 유효하지 않은 데이터를 리다이렉트 시 전달하기 위해 FlashAttribute에 데이터를 추가합니다. (1회용)
            redirectAttrs.addFlashAttribute("title", portfolio.getTitle());
            redirectAttrs.addFlashAttribute("userPr", portfolio.getUserPr());
            redirectAttrs.addFlashAttribute("experience", portfolio.getExperience());
            redirectAttrs.addFlashAttribute("content", portfolio.getContent());

            // 각 필드의 에러를 FlashAttribute에 추가합니다.
            for(var err : result.getFieldErrors()){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }


            return "redirect:/portfolio/write";
        }
        User user = userDetails.getUser();
        portfolio.setUser(user);
        System.out.println(portfolio);
        // 검증이 성공하면 Coupon 객체를 이용하여 수정을 수행하고, 결과를 Model에 담습니다.
        model.addAttribute("result", portfolioService.write(portfolio));

        // 수정 결과를 보여주는 뷰로 이동합니다.
        return "portfolio/writeOk";
    }


    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        Portfolio portfolio = portfolioService.detail(id);
        model.addAttribute("portfolio", portfolio);
        return "portfolio/update";
    }

    @PostMapping("/update")
    public String updateOk(
             @Valid Portfolio portfolio
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs
    ){
        if(result.hasErrors()){

            redirectAttrs.addFlashAttribute("title", portfolio.getTitle());
            redirectAttrs.addFlashAttribute("userPr", portfolio.getUserPr());
            redirectAttrs.addFlashAttribute("experience", portfolio.getExperience());
            redirectAttrs.addFlashAttribute("content", portfolio.getContent());

            for(var err : result.getFieldErrors()){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/portfolio/update/" + portfolio.getPostId();
        }

        System.out.println(portfolio);

        model.addAttribute("result", portfolioService.update(portfolio));
        return "portfolio/updateOk";
    }






    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        portfolioService.increaseViewCount(id.intValue());
        model.addAttribute("portfolio", portfolioService.detail(id));
        return "portfolio/detail";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("initBinder() 호출");
        binder.setValidator(new PortfolioValidator());
    }

    // 페이징
    // pageRows 변경시 동작
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows){
        U.getSession().setAttribute("pageRows", pageRows);
        return "redirect:/portfolio/list?page=" + page;
    }

}
