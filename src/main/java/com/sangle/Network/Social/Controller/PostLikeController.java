package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.Service.PostLikeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class PostLikeController {
    @Autowired
    PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ApiResponse<?> toggleLike(@PathVariable Long postId) {
        boolean liked = postLikeService.toggleLike(postId);
        String message = liked ? "Đã like bài viết" : "Đã bỏ like bài viết";

        return ApiResponse.builder()
                .result(liked)
                .message(message)
                .build();
    }
}
