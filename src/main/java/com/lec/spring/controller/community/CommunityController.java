package com.lec.spring.controller.community;

import com.lec.spring.domain.community.Feed;
import com.lec.spring.domain.community.FeedValidator;
import com.lec.spring.service.community.FeedService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController {
    FeedService feedService;

    public CommunityController(FeedService feedService) {
        this.feedService = feedService;
    }

    @RequestMapping("/list")
    public String list(Integer page, Model model) {
        feedService.list(page, model);
        return "community/communityMainPage";
    }

    @RequestMapping("/search")
    public String search(
            String searchOption
            , String keyword
            , Integer page
            , Model model
    ) {

        feedService.listByOption(searchOption, keyword, page, model);

        return "community/communityMainPage";
    }

    // like 기능
    @RequestMapping("/like")
    public String likeCount(
            @RequestBody String requsetJSON,
            HttpServletResponse response,
            Model model
    ) {
        System.out.println("request json : " + requsetJSON);

        return null;
    }


    @GetMapping("/write")
    public String write() {
        return "community/communityWrite";
    }

    @PostMapping("/write")
    public String writeOk(
            @RequestParam List<MultipartFile> files   // 첨부 파일
            , @Valid Feed feed
            , Long userId
            , BindingResult result
            , Model model   // 매개변수 선언시 BindingResult 보다 Model 을 뒤에 두어야 한다.
            , RedirectAttributes redirectAttrs
    ){
        // validation 에러가 있었다면 redirect 할거다!
        if(result.hasErrors()){
            redirectAttrs.addFlashAttribute("feedTitle", feed.getFeedTitle());
            redirectAttrs.addFlashAttribute("feedContent", feed.getFeedContent());
            redirectAttrs.addFlashAttribute("tagList", feed.getTagList());

            for(var err : result.getFieldErrors()){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/community/write";
        }

        model.addAttribute("result", feedService.writeFeed(feed, userId, files));

        if(feed.getFeedState().equals("comp"))
            return "redirect:/comp/list";

        return "redirect:/temp/list";
    }

    @GetMapping("/update/{id}")
    public String update(
            @PathVariable Long id,
            Model model
    ) {
        Feed feed = feedService.findFeedById(id);

        // 태그 앞에 # 없애주기
        if(!feed.getTagList().isEmpty())
            feed.setTagList(feed.getTagList().replaceAll(" #", ", ").substring(1));

        model.addAttribute("feed", feed);
        System.out.println("feed state : " + feed.getFeedState());

        return "community/communityUpdate";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam List<MultipartFile> newFiles  // 새로 추가될 첨부파일들
            , Long[] deleteList    // 삭제될 파일들
            , @Valid Feed feed
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttrs
    ){
        if(result.hasErrors()){

            redirectAttrs.addFlashAttribute("feedTitle", feed.getFeedTitle());
            redirectAttrs.addFlashAttribute("feedContent", feed.getFeedContent());
            redirectAttrs.addFlashAttribute("tagList", feed.getTagList());

            for(var err : result.getFieldErrors()){
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/community/update/" + feed.getFeedId();
        }


        model.addAttribute("result", feedService.updateFeed(feed, newFiles, deleteList));

        if(feed.getFeedState().equals("comp"))
            return "redirect:/comp/list";

        return "redirect:/temp/list";
    }

    @PostMapping("/delete")
    public String delete(
            Long feedId,
            Model model
    ) {
        int result = feedService.trashFeed(feedId);
        model.addAttribute("result", result);

        return "redirect:/community/list";
    }

    @PostMapping("/shortContent")
    @ResponseBody
    public String shortContent(
            @RequestBody ShortContent request
    )  {
        if(request.getIsShort()) {
            return feedService.findFeedById(request.getFeedId()).getShortContent();
        }
        return feedService.findFeedById(request.getFeedId()).getFeedContent();
    }

    @Data
    static public class ShortContent {
        private Boolean isShort;
        private Long feedId;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new FeedValidator());
    }


}
