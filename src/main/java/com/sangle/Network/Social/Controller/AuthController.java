package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.DTO.Request.LoginRequest;
import com.sangle.Network.Social.DTO.Response.LoginResponse;
import com.sangle.Network.Social.Service.AuthService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    ApiResponse<LoginResponse> login(@RequestBody LoginRequest request)
    {
        return ApiResponse.<LoginResponse>builder()
                .message("login success")
                .result(authService.login(request))
                .build();
    }
}
