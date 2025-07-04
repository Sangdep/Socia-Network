package com.sangle.Network.Social.Repository;

import com.sangle.Network.Social.Entity.Post;
import com.sangle.Network.Social.Entity.PostLike;
import com.sangle.Network.Social.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
}
