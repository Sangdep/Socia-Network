package com.sangle.Network.Social.Mapper;

import com.sangle.Network.Social.DTO.Response.UserProfileResponse;
import com.sangle.Network.Social.Entity.UserProfile;
import com.sangle.Network.Social.Enum.FriendShipStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface UserProfileMapper {

    @Mapping(source = "avatarUrl", target = "avatarUrl")
    UserProfileResponse toUserProfileReponse(UserProfile profile);
    // Method để map thêm status
    default UserProfileResponse toUserProfileReponse(UserProfile profile, FriendShipStatus status) {
        UserProfileResponse response = toUserProfileReponse(profile);
        response.setStatus(status);
        return response;
    }
}
