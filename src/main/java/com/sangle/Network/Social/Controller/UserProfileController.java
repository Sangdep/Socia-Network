package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.DTO.Request.UpdateProfileRequest;
import com.sangle.Network.Social.DTO.Response.UserProfileResponse;
import com.sangle.Network.Social.Entity.UserProfile;
import com.sangle.Network.Social.Service.UserProfileService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.io.IOException;

@RestController
@RequestMapping("/userProfiles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @PutMapping( value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<UserProfileResponse>updateProfile(
                                                  @ModelAttribute UpdateProfileRequest request) throws IOException {
        return ApiResponse.<UserProfileResponse>builder()
                .message("update success")
                .result(userProfileService.updateProfile(request))
                .build();
    }

    @GetMapping("/get-profile/{id}")
    ApiResponse<UserProfileResponse>getProfileById(@PathVariable Long id)
    {
        return ApiResponse.<UserProfileResponse>builder()
                .message("get profile by id")
                .result(userProfileService.getProfileById(id))
                .build();
    }

    @GetMapping("/get-my-profile")
    ApiResponse<UserProfileResponse> getMyProfile()
    {
        return ApiResponse.<UserProfileResponse>builder()
                .message("get my profile success")
                .result(userProfileService.getMyProfile())
                .build();
    }


}
