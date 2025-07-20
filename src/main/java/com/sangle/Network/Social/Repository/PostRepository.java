
package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Additional query methods can be defined here if needed

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByUser_UsernameOrderByCreatedAtDesc(String username);
}
