package com.sangle.Network.Social.Service;

import com.sangle.Network.Social.DTO.Request.ConversationRequest;
import com.sangle.Network.Social.DTO.Request.CreateGroupConversationRequest;
import com.sangle.Network.Social.DTO.Response.ConversationResponse;
import com.sangle.Network.Social.Entity.Chat.Conversation;
import com.sangle.Network.Social.Entity.Chat.ConversationParticipant;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Mapper.ConversationMapper;
import com.sangle.Network.Social.Repository.ConversationParticipantRepository;
import com.sangle.Network.Social.Repository.ConversationRepository;
import com.sangle.Network.Social.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE ,makeFinal = true)
@Slf4j
public class ConversationService {

      ConversationRepository conversationRepository;
      UserRepository userRepository;
      ConversationParticipantRepository conversationParticipantRepository;
      ConversationMapper conversationMapper;

      public ConversationResponse createOneToOne(Long  partnerId)
      {
          var username = SecurityContextHolder.getContext().getAuthentication().getName();

          User currentUser = userRepository.findByUsername(username)
                  .orElseThrow(() -> new RuntimeException("Current user not found"));


          Long currentUserId = currentUser.getId();


          //kiem tra xem conversation exist . neu co thi get ra
          Optional<Conversation> existing = conversationRepository.findOneToOneConversation(currentUserId, partnerId);
          if (existing.isPresent()) {
              return conversationMapper.toDto(existing.get());
          }

          Conversation conversation = Conversation.builder()
                  .isGroup(false)
                  .build();
          conversationRepository.save(conversation);

          User partnerUser = userRepository.findById(partnerId)
                  .orElseThrow(() -> new RuntimeException("Partner not found"));

          List<ConversationParticipant> participants = List.of(
                  ConversationParticipant.builder()
                          .conversation(conversation)
                          .user(currentUser)
                          .joinedAt(LocalDateTime.now())
                          .build(),
                  ConversationParticipant.builder()
                          .conversation(conversation)
                          .user(partnerUser)
                          .joinedAt(LocalDateTime.now())
                          .build()
          );

          conversationParticipantRepository.saveAll(participants);

          // Load lại participants nếu cần
          conversation.setParticipants(participants);

          return conversationMapper.toDto(conversation);
      }


    public ConversationResponse createGroupConversation(CreateGroupConversationRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        // Tạo cuộc trò chuyện nhóm
        Conversation conversation = Conversation.builder()
                .isGroup(true)
                .name(request.getName())
                .build();
        conversationRepository.save(conversation);

        // Lấy danh sách người dùng từ participantIds
        List<User> members = userRepository.findAllById(request.getParticipantIds());

        if (members.size() != request.getParticipantIds().size()) {
            throw new RuntimeException("Some participants not found");
        }

        // Tạo danh sách participants (bao gồm người tạo)
        List<ConversationParticipant> participants = new ArrayList<>();

        // Người tạo là admin
        participants.add(
                ConversationParticipant.builder()
                        .conversation(conversation)
                        .user(currentUser)
                        .joinedAt(LocalDateTime.now())
                        .isAdmin(true)
                        .hasLeft(false)
                        .isMuted(false)
                        .build()
        );

        // Những thành viên còn lại
        for (User user : members) {
            participants.add(
                    ConversationParticipant.builder()
                            .conversation(conversation)
                            .user(user)
                            .joinedAt(LocalDateTime.now())
                            .isAdmin(false)
                            .hasLeft(false)
                            .isMuted(false)
                            .build()
            );
        }

        // Lưu participants
        conversationParticipantRepository.saveAll(participants);

        // Gán lại để mapping dto chính xác
        conversation.setParticipants(participants);

        return conversationMapper.toDto(conversation);
    }


    @Transactional
    public ConversationResponse addMembersToGroup(Long conversationId, List<Long> userIdsToAdd) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        if (!conversation.isGroup()) {
            throw new RuntimeException("Cannot add members to a 1-1 conversation");
        }

        List<User> users = userRepository.findAllById(userIdsToAdd);

        List<Long> existingUserIds = conversation.getParticipants().stream()
                .map(participant -> participant.getUser().getId())
                .toList();

        List<ConversationParticipant> newParticipants = users.stream()
                .filter(user -> !existingUserIds.contains(user.getId()))
                .map(user -> ConversationParticipant.builder()
                        .conversation(conversation)
                        .user(user)
                        .joinedAt(LocalDateTime.now())
                        .isAdmin(false)
                        .isMuted(false)
                        .hasLeft(false)
                        .build()
                ).toList();

        conversationParticipantRepository.saveAll(newParticipants);

        // Optional: cập nhật lại danh sách participants trong conversation
        conversation.getParticipants().addAll(newParticipants);

        return conversationMapper.toDto(conversation);
    }




}
