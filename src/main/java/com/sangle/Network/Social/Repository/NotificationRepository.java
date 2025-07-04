package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
