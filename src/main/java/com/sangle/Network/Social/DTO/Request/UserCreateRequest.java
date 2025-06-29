package com.sangle.Network.Social.DTO.Request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCreateRequest {

    @Email(message = "đúng định dạng email")
    @NotBlank
    String email;
    String username;
    @Size(min = 8, message = "ít nhất là 8 kí tự")
    String password;
    String fullName;
}
