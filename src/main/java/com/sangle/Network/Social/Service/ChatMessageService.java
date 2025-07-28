package com.sangle.Network.Social.Service;

import com.sangle.Network.Social.DTO.Request.ChatMessageRequest;
import com.sangle.Network.Social.DTO.Response.ChatMessageResponse;
import com.sangle.Network.Social.Entity.Chat.ChatMessage;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Entity.UserProfile;
import com.sangle.Network.Social.Exception.AppException;
import com.sangle.Network.Social.Exception.ErorrCode;
import com.sangle.Network.Social.Repository.ChatMessageRepository;
import com.sangle.Network.Social.Repository.ConversationRepository;
import com.sangle.Network.Social.Repository.UserProfileRepository;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ChatMessageService {
    ChatMessageRepository chatMessageRepository;
    ConversationRepository conversationRepository;
    UserProfileRepository userProfileRepository;
    UserRepository userRepository;

    public ChatMessageResponse creatChat(ChatMessageRequest request)
    {
        var auth = SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(auth)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        Long userId= user.getId();
        // Validate
        var conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(p -> p.getUser().getId().equals(user.getId()));

        if (!isParticipant) {
            throw new RuntimeException("User not in conversation");
        }

        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));

        // Save message
        ChatMessage chatMessage= ChatMessage.builder()
                .content(request.getContent())
                .conversation(conversation)
                .sentAt(LocalDateTime.now())
                .sender(user)
                .build();
        chatMessageRepository.save(chatMessage);

        // Trả về response
        return ChatMessageResponse.builder()
                .conversationId(request.getConversationId())
                .content(chatMessage.getContent())
                .senderId(userId)
                .senderName(userProfile.getFullName())
                .avatar(userProfile.getAvatarUrl())
                .me(true)
                .createdDate(chatMessage.getSentAt()
                        .atZone(ZoneId.systemDefault()) // hoặc ZoneOffset.UTC
                        .toInstant())
                .build();
    }

    public List<ChatMessageResponse> getChatHistory(Long conversationId) {
        // Lấy người dùng hiện tại
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        // Kiểm tra conversation có tồn tại không
        var conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // Kiểm tra người dùng có tham gia không
        boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(p -> p.getUser().getId().equals(user.getId()));
        if (!isParticipant) {
            throw new RuntimeException("You are not a participant in this conversation");
        }

        // Lấy userProfile để hiển thị avatar và tên
        UserProfile currentUserProfile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        // Lấy toàn bộ message của conversation theo thời gian tăng dần
        List<ChatMessage> messages = chatMessageRepository.findByConversationIdOrderBySentAtAsc(conversationId);

        return messages.stream().map(msg -> {
            User sender = msg.getSender();
            UserProfile senderProfile = userProfileRepository.findByUserId(sender.getId())
                    .orElse(null); // Nếu không tìm thấy profile thì avatar = null, name = username

            return ChatMessageResponse.builder()
                    .conversationId(conversationId)
                    .content(msg.getContent())
                    .senderId(sender.getId())
                    .senderName(senderProfile != null ? senderProfile.getFullName() : sender.getUsername())
                    .avatar(senderProfile != null ? senderProfile.getAvatarUrl() : null)
                    .me(sender.getId().equals(user.getId()))
                    .createdDate(msg.getSentAt().atZone(ZoneId.systemDefault()).toInstant())
                    .build();
        }).collect(Collectors.toList());
    }

}
