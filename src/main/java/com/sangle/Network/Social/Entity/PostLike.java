package com.sangle.Network.Social.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "post_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    @Column(name = "liked_at", nullable = false)
    LocalDateTime likedAt = LocalDateTime.now();



}
