package com.sangle.Network.Social.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sangle.Network.Social.Enum.FriendShipStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendShipResponse {
    Long id;
    UserSimpleResponse sender;
    UserSimpleResponse receiver;
    FriendShipStatus status;
    LocalDateTime createdAt;
}
