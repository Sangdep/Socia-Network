package com.sangle.Network.Social.Service;

import com.sangle.Network.Social.DTO.Request.CommentRequest;
import com.sangle.Network.Social.DTO.Response.CommentResponse;
import com.sangle.Network.Social.Entity.Comment;
import com.sangle.Network.Social.Entity.Notification;
import com.sangle.Network.Social.Entity.Post;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Enum.NotificationType;
import com.sangle.Network.Social.Exception.AppException;
import com.sangle.Network.Social.Exception.ErorrCode;
import com.sangle.Network.Social.Mapper.CommentMapper;
import com.sangle.Network.Social.Repository.CommentRepository;
import com.sangle.Network.Social.Repository.NotificationRepository;
import com.sangle.Network.Social.Repository.PostRepository;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE ,makeFinal = true)
@Slf4j
public class CommentService {

    CommentRepository commentRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;
    NotificationRepository notificationRepository;


    public CommentResponse createComment(Long postId, CommentRequest request)
    {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user= userRepository.findByUsername(name)
                .orElseThrow(()-> new AppException(ErorrCode.USER_NOT_FOUND));

        Post post= postRepository.findById(postId)
                .orElseThrow(()-> new AppException(ErorrCode.POST_NOT_FOUND));
        Comment comment= Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .build();

        if(request.getParentCommentId()!=null)
        {
            Comment commentParent=commentRepository
                    .findById(request.getParentCommentId())
                        .orElseThrow(() -> new AppException(ErorrCode.COMMENT_NOT_FOUND));
            comment.setParentComment(commentParent);

        }
        post.setCommentsCount(post.getCommentsCount() +1);
        postRepository.save(post);

        // Lưu comment trước rồi mới tạo thông báo
        Comment savedComment = commentRepository.save(comment);

        //gui thong bao neu nguoi binh luan ko phai la chu post
        if (!post.getUser().getId().equals(user.getId()))
        {
            Notification noti= Notification.builder()
                    .receiver(post.getUser())
                    .sender(user)
                    .type(NotificationType.COMMENT)
                    .content(user.getUsername() + " Đã bình luận bài viết của bạn")
                    .build();
            notificationRepository.save(noti);
        }
        return commentMapper.toCommentResponse(savedComment);
    }


    public List<CommentResponse> getAllCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErorrCode.POST_NOT_FOUND));

        List<Comment> rootComments = commentRepository
                .findByPostIdAndParentCommentIsNullOrderByCreatedAtDesc(postId);

        return rootComments.stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse toCommentResponse(Comment comment) {
        List<CommentResponse> replyResponses = comment.getReplies().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed()) // mới nhất lên đầu
                .map(this::toCommentResponse) // đệ quy nếu có cấp nhiều hơn
                .collect(Collectors.toList());

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .likesCount(comment.getLikesCount())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .postId(comment.getPost().getId())
                .replies(replyResponses)
                .build();
    }

}
