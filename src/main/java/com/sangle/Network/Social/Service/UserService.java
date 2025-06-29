package com.sangle.Network.Social.Service;

import com.sangle.Network.Social.DTO.Request.UserCreateRequest;
import com.sangle.Network.Social.DTO.Response.UserResponse;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Entity.UserProfile;
import com.sangle.Network.Social.Enum.Role;
import com.sangle.Network.Social.Mapper.UserMapper;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;


    public UserResponse userCreate(UserCreateRequest request)
    {
        if (userRepository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("fix sau");
        }

        if (userRepository.existsByUsername(request.getUsername()))
        {
            throw new RuntimeException("customer sau");
        }

        User user= userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> role= new HashSet<>();
        role.add(Role.USER.name());
        user.setRole(role);

        UserProfile userProfile= userMapper.toProfile(request);
        userProfile.setUser(user);

        user.setUserProfile(userProfile);

        return userMapper.toUserDTO(userRepository.save(user));
    }

}
