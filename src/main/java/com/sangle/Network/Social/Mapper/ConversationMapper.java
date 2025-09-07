package com.sangle.Network.Social.Mapper;

import com.sangle.Network.Social.DTO.Response.ConversationResponse;
import com.sangle.Network.Social.Entity.Chat.Conversation;
import com.sangle.Network.Social.Entity.Chat.ConversationParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    @Mapping(source = "participants", target = "participants")
    @Mapping(source = "id", target = "conversationId")
    ConversationResponse toDto(Conversation conversation);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.userProfile.fullName", target = "fullName")
    @Mapping(source = "user.userProfile.avatarUrl", target = "avatarUrl")
    ConversationResponse.ParticipantInfo toParticipantDto(ConversationParticipant participant);

    List<ConversationResponse.ParticipantInfo> toParticipantDtoList(List<ConversationParticipant> participants);
}
