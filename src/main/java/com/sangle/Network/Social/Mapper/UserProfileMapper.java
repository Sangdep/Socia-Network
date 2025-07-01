package com.sangle.Network.Social.Mapper;

import com.sangle.Network.Social.DTO.Response.UserProfileResponse;
import com.sangle.Network.Social.Entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface UserProfileMapper {

    @Mapping(source = "avatarUrl", target = "avatarUrl")
    UserProfileResponse toUserProfileReponse(UserProfile profile);
}
