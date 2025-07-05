package com.sangle.Network.Social.Mapper;

import com.sangle.Network.Social.DTO.Response.CommentResponse;
import com.sangle.Network.Social.Entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "post.id" ,target = "postId")
    @Mapping(source = "user.id" ,target = "userId")
    @Mapping(source = "user.username" ,target = "username")
    @Mapping(source = "parentComment.id" ,target = "parentCommentId")
    CommentResponse toCommentResponse(Comment comment);


}
