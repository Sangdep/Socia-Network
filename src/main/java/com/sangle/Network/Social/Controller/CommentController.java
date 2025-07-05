package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.DTO.Request.CommentRequest;
import com.sangle.Network.Social.DTO.Response.CommentResponse;
import com.sangle.Network.Social.Service.CommentService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/create-comment/{postId}")
    ApiResponse<CommentResponse>createComment(@PathVariable Long postId, @RequestBody CommentRequest request)
    {
        return ApiResponse.<CommentResponse>builder()
                .message("created comment successfuly")
                .result(commentService.createComment(postId,request))
                .build();
    }

    @GetMapping("/get-comment-by-post/{postId}")
    ApiResponse<List<CommentResponse>>getAllComment(@PathVariable Long postId)
    {
        return ApiResponse.<List<CommentResponse>>builder()
                .message("get all comment ")
                .result(commentService.getAllCommentsByPost(postId))
                .build();
    }
}
