package com.sangle.Network.Social.Mapper;

import com.sangle.Network.Social.DTO.Response.FriendShipResponse;
import com.sangle.Network.Social.DTO.Response.UserSimpleResponse;
import com.sangle.Network.Social.Entity.FriendShip;
import com.sangle.Network.Social.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendShipMapper {

    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "receiver", target = "receiver")
    FriendShipResponse toFriendResponse(FriendShip friendShip);
    UserSimpleResponse toSimple(User user);
}
