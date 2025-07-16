package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,String> {
}
