package com.lec.spring.controller.community;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.service.community.FeedService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comp")
public class CompFeedController {

    FeedService feedService;

    public CompFeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    // 완료글 목록 보여주기
    @RequestMapping("/list")
    public String list(Integer page,
                       Model model,
                       @AuthenticationPrincipal PrincipalDetails userDetails) {
        if(userDetails != null)
            feedService.listByUserId(userDetails.getUser().getId(), page, model, "comp");
        return "community/myCompFeed";
    }

    // 휴지통으로 이동
    @RequestMapping("/deleteAll")
    public String deleteAll(Model model, @AuthenticationPrincipal PrincipalDetails userDetails) {
        int result = feedService.trashFeedAllByUserId(userDetails.getUser().getId());
        model.addAttribute("result", result);

        return "redirect:/comp/list";
    }

    @PostMapping("/delete")
    public String delete(
            Long feedId,
            Model model
    ) {
        int result = feedService.trashFeed(feedId);
        model.addAttribute("result", result);

        return "redirect:/comp/list";
    }

}
