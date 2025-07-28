package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.Chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findByConversationIdOrderBySentAtAsc(Long conversationId);

}
