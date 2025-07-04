
package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media,Long> {
}
