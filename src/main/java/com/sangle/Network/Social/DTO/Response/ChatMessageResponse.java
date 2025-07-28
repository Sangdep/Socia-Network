package com.sangle.Network.Social.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
     Long conversationId;
     String content;
     Long senderId;
     String senderName;
     String avatar;
     boolean me;
     Instant createdDate;
}
