package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.Chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    @Query("""
           SELECT c FROM Conversation c 
           WHERE c.isGroup = false AND 
                 EXISTS (SELECT 1 FROM c.participants p1 WHERE p1.user.id = :userId1)
               AND EXISTS (SELECT 1 FROM c.participants p2 WHERE p2.user.id = :userId2)
           """)
    Optional<Conversation> findOneToOneConversation(@Param("userId1") Long userId1,
                                                    @Param("userId2") Long userId2);
}
