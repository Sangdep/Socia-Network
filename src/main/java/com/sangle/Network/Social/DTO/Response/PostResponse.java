
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
     Long id;
     UserProfileResponse user;
     String content;
     PostType postType;
     Privacy privacy;
     String location;
     boolean isDeleted;
     int likesCount;
     int commentsCount;
     int sharesCount;
     boolean isLikedByCurrentUser;
     List<MediaResponse> mediaList;
     LocalDateTime createdAt;
}
