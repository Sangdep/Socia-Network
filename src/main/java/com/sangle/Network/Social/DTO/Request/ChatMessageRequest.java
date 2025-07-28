package com.sangle.Network.Social.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
     Long conversationId;
     String content;
}
