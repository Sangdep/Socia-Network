package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.FriendShip;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Enum.FriendShipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip,Long> {

    // Tìm quan hệ từ sender → receiver
    Optional<FriendShip> findBySenderAndReceiver(User sender, User receiver);

    // Lấy danh sách lời mời đến (receiver là người nhận)
    List<FriendShip> findByReceiverAndStatus(User receiver, FriendShipStatus status);

    // Kiểm tra đã tồn tại lời mời sender → receiver với status chưa
    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, FriendShipStatus status);
}
