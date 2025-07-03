
package com.sangle.Network.Social.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="post_media")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "media_type", nullable = false)
    private String mediaType; // Ví dụ: "image", "video"

    @Column(name = "file_path", nullable = false)
    private String filePath; // Đường dẫn/URL đầy đủ

    @Column(name = "file_name")
    private String fileName; // Tên tệp gốc

    @Column(name = "file_size")
    private Long fileSize; // Kích thước tính bằng byte

    @Column(name = "mime_type")
    private String mimeType; // Ví dụ: "image/jpeg", "video/mp4"

    private String caption; // Chú thích tùy chọn cho phương tiện

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
