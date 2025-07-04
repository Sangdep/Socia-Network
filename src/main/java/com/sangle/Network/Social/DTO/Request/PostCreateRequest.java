
package com.sangle.Network.Social.DTO.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sangle.Network.Social.Enum.PostType;
import com.sangle.Network.Social.Enum.Privacy;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCreateRequest {
    @NotBlank(message = "Nội dung bài viết không được để trống")
    @Size(max = 500, message = "Nội dung bài viết không được quá 500 ký tự")
    private String content;

    @NotNull(message = "Loại bài viết không được để trống")
    private PostType postType;

    @NotNull(message = "Quyền riêng tư không được để trống")
    private Privacy privacy;

    private String location;
}
