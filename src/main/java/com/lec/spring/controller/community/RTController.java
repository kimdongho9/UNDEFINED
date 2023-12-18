package com.lec.spring.controller.community;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.service.community.FeedService;
import com.lec.spring.service.community.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class RTController {

    LikeService likeService;
    FeedService feedService;

    @Autowired
    public RTController(LikeService likeService, FeedService feedService) {
        this.likeService = likeService;
        this.feedService = feedService;
    }

    @PostMapping("/like")
    public Integer like(
            @RequestParam Boolean isLike,
            @RequestParam Long feedId,
            @RequestParam Long userId
    ) {


        if(isLike) {
            likeService.addLike(userId, feedId);
        } else {
            likeService.removeLike(userId, feedId);
        }

        return likeService.countLike(feedId);
    }

    @PostMapping("/shortContent")
    public String shortContent(
            @RequestBody String request
    ) throws JsonProcessingException {
//        System.out.println("????");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(request);

        if(node.get("isShort").asBoolean()) {
            return feedService.findFeedById(node.get("feedId").asLong()).getShortContent();
        }
        return feedService.findFeedById(node.get("feedId").asLong()).getFeedContent();
    }
}
















