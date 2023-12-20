package com.lec.spring.controller.study;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.study.*;
import com.lec.spring.service.study.ChatService;
import com.lec.spring.service.study.StudyService;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/study") // repository 보면서
public class StudyController {

    public StudyService studyService;
    private ChatService chatService;
    private Map<String, String> userSessions = new ConcurrentHashMap<>(); // 각 세션에 사용자 이름을 매핑하는 맵

    @Autowired
    public StudyController(StudyService studyService, ChatService chatService) {
        this.studyService = studyService;
        this.chatService = chatService;
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
            redirectAttrs.addFlashAttribute("dataForStartdate", post.getDataForStartdate());
            redirectAttrs.addFlashAttribute("dataForEnddate", post.getDataForEnddate());
            redirectAttrs.addFlashAttribute("content", post.getContent());
            redirectAttrs.addFlashAttribute("member", post.getMember());
            redirectAttrs.addFlashAttribute("position", post.getPosition());


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
    public String detail(@AuthenticationPrincipal PrincipalDetails userDetails, @PathVariable Long id, Model model) {
        Post post = studyService.detail(id);
        model.addAttribute("post", post);

        List<Chat> list = chatService.findChatByPostId(id.intValue());
        System.out.println(list);
        model.addAttribute("chatlist", list);

        if(userDetails != null)
            model.addAttribute("user", userDetails.getUser());

        return "study/detail";
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message, HttpSession session) {
        log.info("@ChatController, sendMessage(), Message: {}", message); // 메시지 전송 요청에 대한 로그 출력

        String sessionId = session.getId(); // 현재 세션의 ID를 가져옴
        String username = userSessions.get(sessionId); // 세션 ID를 기반으로 사용자 이름을 가져옴


        // 세션에 연결된 사용자가 없는 경우, 새로운 사용자 이름 생성 및 맵에 추가
        if (username == null) {
            username = generateUniqueUserName(); // 유니크한 사용자 이름 생성
            userSessions.put(sessionId, username); // 맵에 사용자 이름 추가
        }

        // 현재 시간을 기반으로 타임스탬프 생성
        String timestamp = generateTimestamp(); // 타임스탬프 생성

        // 메시지에 사용자 이름과 타임스탬프를 추가하여 생성
        String messageWithTimestamp = username + ": " + message + " - " + timestamp;

        return ResponseEntity.ok(messageWithTimestamp); // 메시지 반환
    }


    // 유니크한 사용자 이름 생성
    private String generateUniqueUserName() {
        LocalDateTime now = LocalDateTime.now(); // 현재 시간 정보를 가져옴
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm"); // 패턴 지정
        return "user" + now.format(formatter) + "_" + userCounter++; // 사용자 이름 반환

    }

    // 현재 시간을 기반으로 타임스탬프 생성 (날짜와 시간 표시)
    private String generateTimestamp() {
        LocalDateTime now = LocalDateTime.now(); // 현재 시간 정보를 가져옴
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // 패턴 지정
        return "User" + userCounter + " : d - " + now.format(formatter); // 타임스탬프 반환
    }

    private int userCounter = 0; // 사용자 수 카운트

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
            redirectAttrs.addFlashAttribute("dataForStartdate", post.getDataForStartdate());
            redirectAttrs.addFlashAttribute("dataForEnddate", post.getDataForEnddate());
            redirectAttrs.addFlashAttribute("content", post.getContent());

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
