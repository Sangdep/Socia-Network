package com.sangle.Network.Social.Mapper;


import com.sangle.Network.Social.DTO.Request.PostCreateRequest;
import com.sangle.Network.Social.DTO.Response.PostResponse;
import com.sangle.Network.Social.DTO.Response.UserProfileResponse;
import com.sangle.Network.Social.Entity.Post;
import com.sangle.Network.Social.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface PostMapper {

    //@Mapping(target = "user", ignore = true) dong nay xoa di vi bo qua user ma can map user ra de biet ai tao post
    @Mapping(target = "user", expression = "java(toUserProfileResponseFromUser(post.getUser()))")
    PostResponse toPostResponse(Post post);

    @Mapping(target = "user", ignore = true)
    Post toPost(PostCreateRequest request);

    List<PostResponse> toPostResponsesList(List<Post> posts);


    default UserProfileResponse toUserProfileResponseFromUser(User user) {
        if (user == null) return null;

        UserProfileResponse dto = new UserProfileResponse();
        dto.setId(user.getId());
        dto.setFullName(user.getUserProfile().getFullName()); // Nếu bạn muốn username ở đây, hoặc sửa thành dto.setUsername() nếu cần rõ ràng hơn

        if (user.getUserProfile() != null) {
            dto.setAvatarUrl(user.getUserProfile().getAvatarUrl()); // chỉ set avatar
        }

        return dto;
    }
}