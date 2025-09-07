package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.Request.ChatMessageRequest;
import com.sangle.Network.Social.DTO.Response.ChatMessageResponse;
import com.sangle.Network.Social.Service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class ChatWebsocketController {
    SimpMessagingTemplate messagingTemplate;
    ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage") // Client gửi tới: /app/chat.sendMessage
    public void sendMessage(@Payload ChatMessageRequest messageRequest, Principal principal) {
        String username = principal.getName();
        log.info("usernam in create chat" + username);
        ChatMessageResponse response = chatMessageService.creatChat(username,messageRequest); // Save message
        // Gửi lại cho tất cả client trong cuộc trò chuyện
        String destination = "/topic/conversation." + messageRequest.getConversationId();
        messagingTemplate.convertAndSend(destination, response);
    }
}
