
package com.sangle.Network.Social.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Enum.PostType;
import com.sangle.Network.Social.Enum.Privacy;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {
    private Long id;
    private String user; // Thông tin người dùng tạo bài viết
    private String content;
    private PostType postType;
    private Privacy privacy;
    private String location;
    private boolean isDeleted;
    private int likesCount;
    private int commentsCount;
    private int sharesCount;
    private List<MediaResponse> mediaList;
    private LocalDateTime createdAt;
}
