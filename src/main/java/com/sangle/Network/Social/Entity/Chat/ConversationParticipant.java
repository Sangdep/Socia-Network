package com.sangle.Network.Social.Entity.Chat;

import com.sangle.Network.Social.Entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="ConversationParticipants")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
    @ManyToOne
    @JoinColumn(name = "conversation_id")
     Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "user_id")
     User user;

     String nickname;        // Tên hiển thị riêng trong nhóm
     boolean isAdmin;        // Người tạo nhóm hoặc quản trị viên
     boolean isMuted;        // Tắt thông báo
     boolean hasLeft;        // Đã rời nhóm hay chưa
     LocalDateTime joinedAt; // Ngày tham gia nhóm
}
