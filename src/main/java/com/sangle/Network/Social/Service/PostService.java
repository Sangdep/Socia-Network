
package com.sangle.Network.Social.Service;


import com.sangle.Network.Social.DTO.Request.PostCreateRequest;
import com.sangle.Network.Social.DTO.Response.PostResponse;
import com.sangle.Network.Social.Entity.Media;
import com.sangle.Network.Social.Entity.Post;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Enum.PostType;
import com.sangle.Network.Social.Enum.Privacy;
import com.sangle.Network.Social.Exception.AppException;
import com.sangle.Network.Social.Exception.ErorrCode;
import com.sangle.Network.Social.Mapper.PostMapper;
import com.sangle.Network.Social.Repository.PostRepository;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class PostService {

    final PostRepository postRepository;
    final UserRepository userRepository;
    final PostMapper postMapper;


    @Value("${file.upload-dir}")
    String uploadDir;

    public PostResponse createPost(PostCreateRequest postCreateRequest, List<MultipartFile> mediaFiles) throws IOException {
        // 1. Lấy thông tin người dùng hiện tại từ Security Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Lấy username từ Authentication object

        // 2. Tìm người dùng trong database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND)); // Sử dụng exception tùy chỉnh của bạn

        // 3. Mapping từ PostCreateRequest DTO sang Post Entity
        //fix lai(do ko map duoc user ). nen su dung builder nha ^^
        Post post = Post.builder()
                .content(postCreateRequest.getContent())
                .postType(postCreateRequest.getPostType())
                .privacy(postCreateRequest.getPrivacy())
                .location(postCreateRequest.getLocation())
                .user(user)
                .build();


        // 4. Gán người dùng vào bài đăng
        post.setUser(user);

        // createdAt sẽ được tự động thiết lập bởi @PrePersist trong Post Entity

        // 6. Xử lý và gán các file media
        if (mediaFiles != null && !mediaFiles.isEmpty()) {
            for (MultipartFile file : mediaFiles) {
                // Lưu file và lấy thông tin chi tiết
                Map<String, String> mediaDetails = saveMediaFile(file);

                // Tạo đối tượng Media Entity
                Media media = new Media();
                media.setFilePath(mediaDetails.get("filePath"));
                media.setFileName(mediaDetails.get("fileName"));
                media.setFileSize(Long.valueOf(mediaDetails.get("fileSize")));
                media.setMimeType(mediaDetails.get("mimeType"));
                media.setMediaType(getMediaTypeFromMime(mediaDetails.get("mimeType")));
                // Bạn có thể thêm media.setCaption() nếu có trường caption trong PostCreateRequest hoặc riêng cho từng file

                // Thêm media vào danh sách của post.
                // Phương thức addMedia() trong Post entity sẽ tự động thiết lập mối quan hệ hai chiều
                // và thêm Media vào danh sách mediaList.
                post.addMedia(media);
            }
        }

        // 7. Lưu bài đăng vào database
        // Nhờ CascadeType.ALL trên mối quan hệ @OneToMany từ Post đến Media,
        // khi Post được lưu, tất cả các Media liên quan cũng sẽ tự động được lưu.
        Post savedPost = postRepository.save(post);
        log.info("Bài đăng mới của người dùng {} đã được tạo với ID: {}", username, savedPost.getId());

        // 8. Chuyển đổi Post Entity đã lưu sang PostResponse DTO để trả về
        return postMapper.toPostResponse(savedPost);

    }


    public List<PostResponse> getAll()
    {
        List<Post> post=postRepository.findAll();
        return postMapper.toPostResponsesList(post);
    }




    private Map<String, String> saveMediaFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        Map<String, String> details = new HashMap<>();
        details.put("filePath", "/uploads/" + fileName);
        details.put("fileName", file.getOriginalFilename());
        details.put("fileSize", String.valueOf(file.getSize()));
        details.put("mimeType", file.getContentType());
        return details;
    }
    private String getMediaTypeFromMime(String mimeType) {
        if (mimeType == null) {
            return "unknown";
        }
        if (mimeType.startsWith("image/")) {
            return "image";
        } else if (mimeType.startsWith("video/")) {
            return "video";
        }
        return "other";
    }


}
