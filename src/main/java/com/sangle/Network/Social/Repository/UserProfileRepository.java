package com.sangle.Network.Social.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<com.sangle.Network.Social.Entity.UserProfile,Long> {
}
