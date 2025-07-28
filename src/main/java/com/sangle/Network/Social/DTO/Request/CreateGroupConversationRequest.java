package com.sangle.Network.Social.DTO.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateGroupConversationRequest {
     String name; // Tên nhóm
     List<Long> participantIds; // Danh sách ID người tham gia (không bao gồm người tạo)
}
