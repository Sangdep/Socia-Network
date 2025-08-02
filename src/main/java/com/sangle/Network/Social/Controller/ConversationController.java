package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.DTO.Request.ConversationRequest;
import com.sangle.Network.Social.DTO.Request.CreateGroupConversationRequest;
import com.sangle.Network.Social.DTO.Response.ConversationResponse;
import com.sangle.Network.Social.Service.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class ConversationController {
    ConversationService conversationService;

        // Tạo cuộc trò chuyện 1-1
        @PostMapping("/one-to-one")
        public ApiResponse<ConversationResponse> createOneToOne(@RequestBody ConversationRequest request) {
            ConversationResponse response = conversationService.createOneToOne(request);
            return ApiResponse.<ConversationResponse>builder()
                    .result(response)
                    .build();
        }

    // Tạo nhóm trò chuyện
    @PostMapping("/group")
    public ApiResponse<ConversationResponse> createGroupConversation(
            @RequestBody CreateGroupConversationRequest request) {
        ConversationResponse response = conversationService.createGroupConversation(request);
        return ApiResponse.<ConversationResponse>builder()
                .result(response)
                .build();
    }

    // Thêm thành viên vào nhóm
    @PostMapping("/{conversationId}/add-members")
    public ApiResponse<ConversationResponse> addMembersToGroup(
            @PathVariable Long conversationId,
            @RequestBody List<Long> userIdsToAdd) {
        ConversationResponse response = conversationService.addMembersToGroup(conversationId, userIdsToAdd);
        return ApiResponse.<ConversationResponse>builder()
                .result(response)
                .build();
    }

}
