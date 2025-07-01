package com.sangle.Network.Social.Mapper;

import com.sangle.Network.Social.DTO.Request.UserCreateRequest;
import com.sangle.Network.Social.DTO.Response.UserProfileResponse;
import com.sangle.Network.Social.DTO.Response.UserResponse;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "Spring")
public interface UserMapper {

    @Mapping(target = "userProfile", ignore = true)
    User toUser(UserCreateRequest request);

    @Mapping(target = "user", ignore = true)
    UserProfile toProfile(UserCreateRequest request);

    UserResponse toUserDTO(User user);




}
