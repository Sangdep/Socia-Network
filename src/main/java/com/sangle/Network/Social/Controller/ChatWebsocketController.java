package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.Request.ChatMessageRequest;
import com.sangle.Network.Social.DTO.Response.ChatMessageResponse;
import com.sangle.Network.Social.Service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebsocketController {
    SimpMessagingTemplate messagingTemplate;
    ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage") // Client gửi tới: /app/chat.sendMessage
    public void sendMessage(@Payload ChatMessageRequest messageRequest) {
        ChatMessageResponse response = chatMessageService.creatChat(messageRequest); // Save message
        // Gửi lại cho tất cả client trong cuộc trò chuyện
        String destination = "/topic/conversation." + messageRequest.getConversationId();
        messagingTemplate.convertAndSend(destination, response);
    }
}
