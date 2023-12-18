package com.lec.spring.controller.community;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.service.community.FeedService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trash")
public class DelFeedController {

    FeedService feedService;

    public DelFeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @RequestMapping("/list")
    public String list(Integer page,
                       Model model,
                       @AuthenticationPrincipal PrincipalDetails userDetails
    ) {
        if(userDetails != null)
            feedService.listByUserId(userDetails.getUser().getId(), page, model, "del");

        return "community/myDelFeed";
    }

    // 휴지통 비우기
    @RequestMapping("/deleteAll")
    public String deleteAll(Model model, @AuthenticationPrincipal PrincipalDetails userDetails) {
        int result = feedService.deleteFeedAllByUserId(userDetails.getUser().getId(), "del");
        model.addAttribute("result", result);

        return "redirect:/trash/list";
    }

    // 복원
    @PostMapping("/restore")
    public String restore(
            Long feedId,
            Model model)
    {
        int result = feedService.restoreFeed(feedId);
        model.addAttribute("result", result);

        return "redirect:/trash/list";
    }
    
    // 완전 삭제
    @PostMapping("/delete")
    public String delete(
            Long feedId,
            Model model
    ) {
        int result = feedService.deleteFeed(feedId);
        model.addAttribute("result", result);

        return "redirect:/trash/list";
    }
}
