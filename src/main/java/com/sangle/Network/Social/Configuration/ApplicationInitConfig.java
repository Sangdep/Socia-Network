package com.sangle.Network.Social.Configuration;

import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Enum.Role;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;


    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository)
    {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty())
            {
                var roles=new HashSet<String>();
                roles.add(Role.ADMIN.name());

                User user= User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .role(roles)
                        .build();

                userRepository.save(user);
                log.error("admin has been create with default password: admin ");

            }


        };

    }
}
