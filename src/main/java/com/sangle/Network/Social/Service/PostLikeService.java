package com.sangle.Network.Social.Service;

import com.sangle.Network.Social.Entity.Notification;
import com.sangle.Network.Social.Entity.Post;
import com.sangle.Network.Social.Entity.PostLike;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Enum.NotificationType;
import com.sangle.Network.Social.Exception.AppException;
import com.sangle.Network.Social.Exception.ErorrCode;
import com.sangle.Network.Social.Repository.NotificationRepository;
import com.sangle.Network.Social.Repository.PostLikeRepository;
import com.sangle.Network.Social.Repository.PostRepository;
import com.sangle.Network.Social.Repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE ,makeFinal = true)
@Slf4j
public class PostLikeService {
    PostLikeRepository postLikeRepository;
    UserRepository userRepository;
    PostRepository postRepository;
    NotificationRepository notificationRepository;

    @Transactional
    public Boolean toggleLike(Long postId)
    {
        var name= SecurityContextHolder.getContext().getAuthentication().getName();
        User user= userRepository.findByUsername(name)
                .orElseThrow(()-> new AppException(ErorrCode.USER_NOT_FOUND));

        Post post= postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErorrCode.POST_NOT_FOUND));

        //if post da co user like
        if (postLikeRepository.existsByUserAndPost(user,post))
        {
            postLikeRepository.deleteByUserAndPost(user,post);
            post.setLikesCount(post.getLikesCount() -1);
            postRepository.save(post);
            return false;
        }
        else {
            //map user va post vao postlike
            PostLike postLike= PostLike.builder()
                    .post(post)
                    .user(user)
                    .likedAt(LocalDateTime.now())
                    .build();
            postLikeRepository.save(postLike);
            //Like +1
            post.setLikesCount(post.getLikesCount() + 1);


            //gui thong bao neu nguoi like ko phai la chu post
            if (!post.getUser().getId().equals(user.getId()))
            {
                Notification noti= Notification.builder()
                        .receiver(post.getUser())
                        .sender(user)
                        .type(NotificationType.LIKE)
                        .content(user.getUsername() + " Đã thích bài viết của bạn")
                        .build();
                notificationRepository.save(noti);
            }

            postRepository.save(post);
            return true;
        }

    }
}
