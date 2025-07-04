
package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.DTO.Request.PostCreateRequest;
import com.sangle.Network.Social.DTO.Response.PostResponse;
import com.sangle.Network.Social.Enum.PostType;
import com.sangle.Network.Social.Enum.Privacy;
import com.sangle.Network.Social.Service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class PostController {
    @Autowired
    PostService postService;
    //fix lai
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostResponse> createPost(
            @RequestParam("content") String content,
            @RequestParam("postType") PostType postType,
            @RequestParam("privacy") Privacy privacy,
            @RequestParam(value = "location", required = false) String location,
            @RequestPart(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles
    ) throws IOException {

        PostCreateRequest request = PostCreateRequest.builder()
                .content(content)
                .postType(postType)
                .privacy(privacy)
                .location(location)
                .build();

        return ApiResponse.<PostResponse>builder()
                .message("Post created successfully")
                .result(postService.createPost(request, mediaFiles))
                .build();
    }

    @GetMapping("/get-all-post")
    ApiResponse<List<PostResponse>>getAllPost()
    {
        return ApiResponse.<List<PostResponse>>builder()
                .message("get all post sucessfuly")
                .result(postService.getAll())
                .build();
    }

}
