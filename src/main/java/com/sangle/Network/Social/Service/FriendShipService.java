package com.sangle.Network.Social.Service;

import com.sangle.Network.Social.DTO.Response.FriendShipResponse;
import com.sangle.Network.Social.DTO.Response.UserSimpleResponse;
import com.sangle.Network.Social.Entity.FriendShip;
import com.sangle.Network.Social.Entity.Notification;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Enum.FriendShipStatus;
import com.sangle.Network.Social.Enum.NotificationType;
import com.sangle.Network.Social.Exception.AppException;
import com.sangle.Network.Social.Exception.ErorrCode;
import com.sangle.Network.Social.Mapper.FriendShipMapper;
import com.sangle.Network.Social.Repository.FriendShipRepository;
import com.sangle.Network.Social.Repository.NotificationRepository;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE ,makeFinal = true)
@Slf4j
public class FriendShipService {

    FriendShipMapper friendShipMapper;
    FriendShipRepository friendShipRepository;
    UserRepository userRepository;
    NotificationRepository notificationRepository;

    public FriendShipResponse sendFriendRequest (String ReceiverUsername)
    {
        var senderUsername= SecurityContextHolder.getContext().getAuthentication().getName();
        User sender=userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new AppException(ErorrCode.SENDER_NOT_FOUND));

        User receiver=userRepository.findByUsername(ReceiverUsername)
                .orElseThrow(() -> new AppException(ErorrCode.RECEIVER_NOT_FOUND));

        // Kiểm tra trùng yêu cầu
        if (friendShipRepository.existsBySenderAndReceiver(sender, receiver)
                || friendShipRepository.existsBySenderAndReceiver(receiver, sender)) {
            throw new RuntimeException("Friend request already exists or you're already friends");
        }

        FriendShip friendShip= FriendShip.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendShipStatus.PENDING)
                .build();

        FriendShip saved=friendShipRepository.save(friendShip);

        Notification noti= Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .type(NotificationType.FRIEND_REQUEST)
                .content(sender.getUserProfile().getFullName() + " Đã gửi lời kết bạn cho bạn ")
                .build();
        notificationRepository.save(noti);
        // Map thủ công FriendShip -> FriendShipResponse
        return FriendShipResponse.builder()
                .id(saved.getId())
                .sender(toSimple(sender)) // hoặc dùng mapper.toSimple()
                .receiver(toSimple(receiver))
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();

    }




    public FriendShipResponse acceptFriendRequest(String senderUsername) {
        String receiverUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new AppException(ErorrCode.RECEIVER_NOT_FOUND));

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new AppException(ErorrCode.SENDER_NOT_FOUND));

        FriendShip friendShip = friendShipRepository
                .findBySenderAndReceiverAndStatus(sender, receiver ,FriendShipStatus.PENDING)
                .orElseThrow(() -> new AppException(ErorrCode.FRIEND_REQUEST_NOT_FOUND));

        friendShip.setStatus(FriendShipStatus.ACCEPTED);
        FriendShip saved=friendShipRepository.save(friendShip);

        Notification noti = Notification.builder()
                .receiver(sender) // thông báo cho sender
                .sender(receiver) // người đã chấp nhận
                .type(NotificationType.FRIEND_ACCEPTED)
                .content(receiver.getUserProfile().getFullName() + " đã chấp nhận lời mời kết bạn của bạn")
                .build();
        notificationRepository.save(noti);

        // Map thủ công FriendShip -> FriendShipResponse
        return FriendShipResponse.builder()
                .id(saved.getId())
                .sender(toSimple(sender)) // hoặc dùng mapper.toSimple()
                .receiver(toSimple(receiver))
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }



    public List<UserSimpleResponse> getFriendList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        List<FriendShip> friendships = friendShipRepository.findAllByUserAndStatusAccepted(user.getId());

        return friendships.stream()
                .map(friendship -> {
                    User friend;
                    //neu toi la nguoi gui(sender ) thi sẽ lay ra ban (receiver)
                    if (friendship.getSender().getId().equals(user.getId())) {
                        friend = friendship.getReceiver();

                    }
                    //nguoc lai
                    else {
                        friend = friendship.getSender();
                    }
                    return toSimple(friend);
                })
                .collect(Collectors.toList());
    }

    public List<UserSimpleResponse> getReceiverList()
    {
        var auth=SecurityContextHolder.getContext().getAuthentication().getName();

        User user=userRepository.findByUsername(auth)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        //day la 1 list chua doi tuong
        List<FriendShip> friendShips=friendShipRepository
                .findByReceiverAndStatus(user,FriendShipStatus.PENDING);

        // Map danh sách sender (người gửi lời mời) thành UserSimpleResponse
        List<UserSimpleResponse> senders = friendShips.stream()
                .map(friendShip -> {
                    //lay ra nguoi da gui ket ban den toi
                    User sender = friendShip.getSender();
                    return UserSimpleResponse.builder()
                            .id(sender.getId())
                            .avatarUrl(sender.getUserProfile().getAvatarUrl())
                            .fullName(sender.getUserProfile().getFullName())
                            .build();
                })
                .toList();
        //su dung thay cho foreach

        return senders;

    }

    public void removeFriend(String friendUsername) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        FriendShip friendShip = friendShipRepository
                .findFriendshipBetweenUsers(currentUser.getId(), friend.getId())
                .orElseThrow(() -> new AppException(ErorrCode.FRIEND_REQUEST_NOT_FOUND));

        if (friendShip.getStatus() != FriendShipStatus.ACCEPTED) {
            throw new AppException(ErorrCode.NOT_FRIEND);
        }

        friendShipRepository.delete(friendShip);
    }


    public void blockUser(String targetUsername) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        // Kiểm tra xem đã có quan hệ bạn bè/chặn chưa
        FriendShip existing = friendShipRepository
                .findFriendshipBetweenUsers(currentUser.getId(), targetUser.getId())
                .orElse(null);

        if (existing != null) {
            existing.setStatus(FriendShipStatus.BLOCKED);
            friendShipRepository.save(existing);
        } else {
            FriendShip block = FriendShip.builder()
                    .sender(currentUser)
                    .receiver(targetUser)
                    .status(FriendShipStatus.BLOCKED)
                    .build();
            friendShipRepository.save(block);
        }
    }

    private UserSimpleResponse toSimple(User user) {
        return UserSimpleResponse.builder()
                .id(user.getId())
                .fullName(user.getUserProfile().getFullName())
                .avatarUrl(user.getUserProfile().getAvatarUrl())
                .build();
    }

}
