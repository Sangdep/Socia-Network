package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.DTO.Request.UserCreateRequest;
import com.sangle.Network.Social.DTO.Response.UserResponse;
import com.sangle.Network.Social.Service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    ApiResponse<UserResponse> userCreate(@RequestBody @Valid UserCreateRequest request)
    {
        return ApiResponse.<UserResponse>builder()
                .message("create success")
                .result(userService.userCreate(request))
                .build();
    }
}
