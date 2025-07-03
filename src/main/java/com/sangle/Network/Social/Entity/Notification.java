package com.sangle.Network.Social.Entity;

import com.sangle.Network.Social.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Người nhận thông báo (bắt buộc)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    User receiver;

    // Người gây ra hành động (like, comment...), có thể null nếu hệ thống gửi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    User sender;

    // Loại thông báo: LIKE, COMMENT, FRIEND_REQUEST...
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    NotificationType type;

    // Nội dung hiển thị lên giao diện
    @Column(nullable = false)
    String content;

    // (Tùy chọn) ID đối tượng liên quan (Post, Comment, v.v.)
    @Column(name = "object_id")
    Long objectId;

    // (Tùy chọn) Kiểu đối tượng: POST, COMMENT, MESSAGE,...
    @Column(name = "object_type")
    String objectType;

    // Trạng thái đã đọc hay chưa
    @Column(name = "is_read", nullable = false)
    boolean isRead = false;

    // Ngày tạo thông báo
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
