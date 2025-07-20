package com.sangle.Network.Social.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sangle.Network.Social.Enum.FriendShipStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    Long id; // trùng với id của User
    String fullName;
    String avatarUrl;
    String bio;
    String gender;
    LocalDate birthDate;
    FriendShipStatus status;

}
