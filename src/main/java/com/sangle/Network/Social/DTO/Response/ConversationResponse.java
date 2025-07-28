package com.sangle.Network.Social.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationResponse {
     Long conversationId;
     boolean isGroup;
     List<ParticipantInfo> participants;



    @Data
    @Builder
    public static class ParticipantInfo {
         Long userId;
         String fullName;
         String avatarUrl;
    }

}
