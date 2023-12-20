package com.lec.spring.controller.study;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.study.Favor;
import com.lec.spring.domain.study.Post;
import com.lec.spring.domain.study.PostValidator;
import com.lec.spring.domain.study.Skill;
import com.lec.spring.service.study.StudyService;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/study") // repository 보면서
public class StudyController {

    // 3가지 DI
    // setter, field, constructor
    // 스프링은 field DI 를 권장하지 않는다.
    public StudyService studyService;

    // 생성자가 하나만 있을 때는 Autowired 안 해줘도 자동으로 주입한다.
    //한 번만 사용 할 때
    public StudyController(StudyService studyService) {
        this.studyService = studyService;
        System.out.println("스터디 컨트롤러 생성");
    }

    @GetMapping("/write")
    public void write() {
    }

    @PostMapping("/write")
    public String writeOK(@Valid Post post

            , BindingResult results    //Model을 binding보다는 뒤에 있어야 함.
            , Model model
            , RedirectAttributes redirectAttrs
    ) {
        if (results.hasErrors()) {

            redirectAttrs.addFlashAttribute("title", post.getTitle());
            redirectAttrs.addFlashAttribute("dataForStartdate", post.getStartdate());
            redirectAttrs.addFlashAttribute("dataForEnddate", post.getEnddate());


            for (var err : results.getFieldErrors()) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }
            return "redirect:/study/write";
        }

        //  System.out.println(post);
        model.addAttribute("result", studyService.write(post, post.getSkills()));
        return "study/writeOk";
    }


    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", studyService.detail(id));
        return "study/detail";
    }

    @GetMapping("/list")
    public void list(Integer page, Model model) {
        studyService.list(page, model);
    }

    @GetMapping("/search")
    public String search(
            String keyword,
            Integer page,
            Model model
    ) {
        keyword = setOrGetHttpSession(U.getSession(), keyword, "keyword");
        if (!keyword.isEmpty()) {
            studyService.listByKeyword(page, model, keyword);
        } else {
            studyService.list(page, model);
        }

        return "study/list";
    }

    @GetMapping("/skillSearch")
    public String skillSearch(
            String skill,
            Integer page,
            Model model
    ) {
        skill = setOrGetHttpSession(U.getSession(), skill, "skill");

        if(skill != null && skill.equals("전체")) {
            studyService.list(page, model);
        } else {
            studyService.listBySkill(page, model, skill);
        }

        return "study/list";
    }

    @GetMapping("/favorSearch")
    public String favorSearch(
            @AuthenticationPrincipal PrincipalDetails userDetails,
            String skill,
            String favor,
            Integer page,
            Model model
    ) {
        skill = setOrGetHttpSession(U.getSession(), skill, "skill");
        favor = setOrGetHttpSession(U.getSession(), favor, "favor");

        if(skill.equals("전체") || skill.equals("기술스택")) {
            if(favor.equals("true")) {
                studyService.favorByAll(page, model, skill, favor, userDetails.getUser().getId());
            }else { // 전체 리스트 가지고 와
                studyService.list(page, model);
            }
        } else {
            if(favor.equals("true")) {
                studyService.listByFavor(page, model, skill, favor, userDetails.getUser().getId());
            } else { // 전체 리스트 가지고 와
                studyService.listBySkill(page, model, skill);
            }
        }

        return "study/list";
    }

    private <T> T setOrGetHttpSession(HttpSession session, T value, String attrName) {
        if(value != null) {
            session.setAttribute("session_" +  attrName, value);
        } else {
            value = (T) session.getAttribute("session_" + attrName);
        }
        return value;
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Post post = studyService.selectById(id);
        List<Skill> list = studyService.getSkillsByPostId(id);

        //for(var x : list)System.out.println(x);
        model.addAttribute("post", post);
        model.addAttribute("list", list);

        return "study/update";
    }

    @PostMapping("/update")
    public String updateOk(@Valid Post post
            , BindingResult results    //Model을 binding보다는 뒤에 있어야 함.
            , Model model
            , RedirectAttributes redirectAttrs
    ) {
        if (results.hasErrors()) {

            redirectAttrs.addFlashAttribute("title", post.getTitle());
            redirectAttrs.addFlashAttribute("dataForStartdate", post.getStartdate());
            redirectAttrs.addFlashAttribute("dataForEnddate", post.getEnddate());


            for (var err : results.getFieldErrors()) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }
            return "redirect:/study/update/" + post.getId();
        }

        int result = studyService.update(post, post.getSkills());
        model.addAttribute("result", result);
        return "study/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model) {
        int result = studyService.deleteById(id);
        model.addAttribute("result", result);
        return "study/deleteOk";
    }

    // 페이징
    // pageRows 변경시 동작
    @PostMapping("/pageRows")
    public String pageRows(Integer page, Integer pageRows) {
        U.getSession().setAttribute("pageRows", pageRows);
        return "redirect:/study/list?page=" + page;
    }

    // 즐겨찾기
    @GetMapping("/favorbyuserid")
    @ResponseBody
    public int favorbyuserid(Long userid, Long postid) {
        List<Favor> list = studyService.getFavorByUserId(userid);
        for (var x : list) {
            //System.out.println(x);
            //System.out.println(x.getPostid() == postid);
            if (x.getPostid() == postid) return 1;
        }
        return 0;
    }

    @PostMapping("/Savefavor")
    @ResponseBody
    public int Savefavor(Long userid, Long postid) {
        return studyService.Favsave(postid, userid);
    }

    @PostMapping("/Delfavor")
    @ResponseBody
    public int Delfavor(Long userid, Long postid) {
        return studyService.Favdelete(postid, userid);
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
//        System.out.println("IB()호출");
        binder.setValidator(new PostValidator());
    }
}// end Controller
