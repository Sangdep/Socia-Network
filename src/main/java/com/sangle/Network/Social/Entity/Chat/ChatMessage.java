package com.sangle.Network.Social.Entity.Chat;

import com.sangle.Network.Social.Entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="messages")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     String content;

     LocalDateTime sentAt;

     boolean seen;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
     Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    User sender;
}
