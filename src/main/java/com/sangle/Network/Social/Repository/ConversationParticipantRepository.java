package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.Chat.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {
    List<ConversationParticipant> findByUser_Id(Long userId);
}
