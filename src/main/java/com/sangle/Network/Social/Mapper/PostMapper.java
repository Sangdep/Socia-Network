package com.sangle.Network.Social.Mapper;


import com.sangle.Network.Social.DTO.Request.PostCreateRequest;
import com.sangle.Network.Social.DTO.Response.PostResponse;
import com.sangle.Network.Social.Entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface PostMapper {

    //@Mapping(target = "user", ignore = true) dong nay xoa di vi bo qua user ma can map user ra de biet ai tao post
    @Mapping(source = "user.username", target = "user")
    PostResponse toPostResponse(Post post);

    @Mapping(target = "user", ignore = true)
    Post toPost(PostCreateRequest request);

    List<PostResponse> toPostResponsesList(List<Post> posts);
}