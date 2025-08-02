package com.sangle.Network.Social.Controller;

import com.sangle.Network.Social.DTO.ApiResponse;
import com.sangle.Network.Social.DTO.Response.FriendShipResponse;
import com.sangle.Network.Social.DTO.Response.UserSimpleResponse;
import com.sangle.Network.Social.Entity.FriendShip;
import com.sangle.Network.Social.Service.FriendShipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@FieldDefaults(level = AccessLevel.PRIVATE ,makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class FriendShipController {
    FriendShipService friendShipService;


    @PostMapping("/send-friend/{ReceiverUsername}")
    ApiResponse<FriendShipResponse> sendFriendRequest(@PathVariable String ReceiverUsername)
    {
        return ApiResponse.<FriendShipResponse>builder()
                .message("send friend request success")
                .result(friendShipService.sendFriendRequest(ReceiverUsername))
                .build();
    }

    //fix lai su dung id
    @PostMapping("/accept-friend/{senderUsername}")
    ApiResponse<FriendShipResponse> acceptFriendRequest(@PathVariable String senderUsername)
    {
        return ApiResponse.<FriendShipResponse>builder()
                .message("send friend request success")
                .result(friendShipService.acceptFriendRequest(senderUsername))
                .build();
    }

    @GetMapping("/get-friend-list")
    ApiResponse<List<UserSimpleResponse>>getFriendList()
    {
        return ApiResponse.<List<UserSimpleResponse>>builder()
                .message("get friend list success")
                .result(friendShipService.getFriendList())
                .build();
    }

    @DeleteMapping("/remove-friend/{friendUsername}")
    public ApiResponse<String> removeFriend(@PathVariable String friendUsername) {
        friendShipService.removeFriend(friendUsername);
        return ApiResponse.<String>builder()
                .message("Remove friend success")
                .result("Removed " + friendUsername)
                .build();
    }

    @PostMapping("/block-friend/{friendUsername}")
    public ApiResponse<String> blockFriend(@PathVariable String friendUsername) {
        friendShipService.blockUser(friendUsername);
        return ApiResponse.<String>builder()
                .message("Block friend success")
                .result("Blocked " + friendUsername)
                .build();
    }

    @GetMapping("/get-receive-list")
    ApiResponse<List<UserSimpleResponse>>getReceiverList()
    {
        return ApiResponse.<List<UserSimpleResponse>>builder()
                .message("get receiver list success")
                .result(friendShipService.getReceiverList())
                .build();
    }


}
