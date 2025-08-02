package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.DTO.Request.ChatMessageRequest;
import com.sangle.Network.Social.DTO.Response.ChatMessageResponse;
import com.sangle.Network.Social.Service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatMessages")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class ChatMessageController {

    ChatMessageService chatMessageService;

        @GetMapping("/get-message")
    ApiResponse<List<ChatMessageResponse>> getMessages(
            @RequestParam("conversationId") Long conversationId) {
        return ApiResponse.<List<ChatMessageResponse>>builder()
                .result(chatMessageService.getChatHistory(conversationId))
                .build();
    }

}
