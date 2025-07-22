package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.FriendShip;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Enum.FriendShipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip,Long> {

    // Tìm quan hệ từ sender → receiver
    Optional<FriendShip> findBySenderAndReceiver(User sender, User receiver);

    Optional<FriendShip> findBySender_UsernameAndReceiver_Username(String sender, String receiver);

    List<FriendShip> findByReceiverAndStatus(User receiver, FriendShipStatus status);
    List<FriendShip> findBySenderAndStatus(User sender, FriendShipStatus status);

    List<FriendShip> findByStatusAndSenderOrReceiver(FriendShipStatus status, User sender, User receiver);
    Optional<FriendShip> findBySenderAndReceiverAndStatus(User sender, User receiver, FriendShipStatus status);


    boolean existsBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT f FROM FriendShip f WHERE " +
            "((f.sender.id = :user1Id AND f.receiver.id = :user2Id) " +
            "OR (f.sender.id = :user2Id AND f.receiver.id = :user1Id))")
    Optional<FriendShip> findFriendshipBetweenUsers(Long user1Id, Long user2Id);

    @Query("SELECT f FROM FriendShip f WHERE (f.sender.id = :userId OR f.receiver.id = :userId) AND f.status = 'ACCEPTED'")
    List<FriendShip> findAllByUserAndStatusAccepted(@Param("userId") Long userId);
}
