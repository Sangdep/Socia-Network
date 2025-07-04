package com.sangle.Network.Social.Entity;

import com.sangle.Network.Social.Enum.PostType;
import com.sangle.Network.Social.Enum.Privacy;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="posts")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Giả sử có một thực thể User cho user_id
    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user; // Liên kết với thực thể User

    @Lob // Dành cho nội dung văn bản có khả năng lớn
    @Column(columnDefinition = "TEXT") // Sử dụng kiểu TEXT trong DB cho nội dung lớn hơn
     String content;

    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng String trong DB
    @Column(name = "post_type", nullable = false)
     PostType postType; // Enum: TEXT, IMAGE, VIDEO, ALBUM, v.v.

    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng String trong DB
    @Column(nullable = false)
     Privacy privacy; // Enum: PUBLIC, FRIENDS, ONLY_ME, CUSTOM

    private String location; // Tùy chọn

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // Mặc định là chưa xóa mềm

    @Column(name = "likes_count", nullable = false)
    private int likesCount = 0;

    @Column(name = "comments_count", nullable = false)
    private int commentsCount = 0;

    @Column(name = "shares_count", nullable = false)
    private int sharesCount = 0;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
     List<Media> mediaList = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }



    public void addMedia(Media media) {
        mediaList.add(media);
        media.setPost(this); // Thiết lập liên kết hai chiều
    }
    public void removeMedia(Media media) {
        mediaList.remove(media);
        media.setPost(null);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Giả sử có một thực thể User cho user_id


    @OneToMany(mappedBy = "post" ,fetch = FetchType.LAZY,cascade = CascadeType.ALL , orphanRemoval = true)
    List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Comment>comments= new ArrayList<>();


}