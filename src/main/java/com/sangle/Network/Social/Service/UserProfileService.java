package com.sangle.Network.Social.Service;

import com.sangle.Network.Social.DTO.Request.UpdateProfileRequest;
import com.sangle.Network.Social.DTO.Response.UserProfileResponse;
import com.sangle.Network.Social.Entity.FriendShip;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Entity.UserProfile;
import com.sangle.Network.Social.Enum.FriendShipStatus;
import com.sangle.Network.Social.Exception.AppException;
import com.sangle.Network.Social.Exception.ErorrCode;
import com.sangle.Network.Social.Mapper.UserMapper;
import com.sangle.Network.Social.Mapper.UserProfileMapper;
import com.sangle.Network.Social.Repository.FriendShipRepository;
import com.sangle.Network.Social.Repository.UserProfileRepository;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class UserProfileService {

    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    FileUploadService fileUploadService;
    UserRepository userRepository;
    FriendShipRepository friendShipRepository;

    public UserProfileResponse getProfileById(Long userId)
    {

        //lay user hien tai
        var auth=SecurityContextHolder.getContext().getAuthentication().getName();

        User user=userRepository.findByUsername(auth)
                .orElseThrow(() -> new AppException(ErorrCode.USER_NOT_FOUND));

        UserProfile userProfile= userProfileRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErorrCode.USERPROFILE_NOT_FOUND));

        User user1=userProfile.getUser();

        FriendShipStatus status=getFriendShipStatus(user,user1);


        return userProfileMapper.toUserProfileReponse(userProfile,status);

    }

    public FriendShipStatus getFriendShipStatus(User currentUser, User otherUser) {
        Optional<FriendShip> friendship = friendShipRepository.findBySenderAndReceiver(currentUser, otherUser);
        if (friendship.isPresent()) {
            return friendship.get().getStatus();
        }

        Optional<FriendShip> reverseFriendship = friendShipRepository.findBySenderAndReceiver(otherUser, currentUser);
        return reverseFriendship.map(FriendShip::getStatus).orElse(null);
    }

    //lay profile cua chinh user do
    public UserProfileResponse getMyProfile()
    {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();

        UserProfile userProfile= userProfileRepository.findByUsername(name)
                .orElseThrow(()-> new AppException(ErorrCode.USERPROFILE_NOT_FOUND));

        return userProfileMapper.toUserProfileReponse(userProfile);

    }

    public UserProfileResponse updateProfile( UpdateProfileRequest request) throws IOException {

        var auth=SecurityContextHolder.getContext().getAuthentication().getName();

        UserProfile userProfile = userProfileRepository.findByUsername(auth)
                .orElseThrow(()-> new AppException(ErorrCode.USERPROFILE_NOT_FOUND));

        //map tu updaterequest vao userprofile
        userProfile.setFullName(request.getFullName());
        userProfile.setBio(request.getBio());
        userProfile.setGender(request.getGender());
        userProfile.setBirthDate(request.getBirthDate());

        //goi toi fileUploadservice de chuyen doi tu file sang url
        MultipartFile avatarFile= request.getAvatar();
        if (avatarFile!=null && !avatarFile.isEmpty())
        {
            String avatarurl= fileUploadService.uploadImage(avatarFile);
            userProfile.setAvatarUrl(avatarurl);
        }
        //luu xuong fb
        UserProfile saved= userProfileRepository.save(userProfile);

        //tra ve response
        return userProfileMapper.toUserProfileReponse(saved);

    }

}
