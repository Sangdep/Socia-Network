package com.sangle.Network.Social.Configuration;

import com.sangle.Network.Social.Entity.CustomUserDetails;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Exception.AppException;
import com.sangle.Network.Social.Exception.ErorrCode;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    JwtDecoder jwtDecoder;
    UserRepository userRepository;



    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // topic: group, queue: private
        config.setApplicationDestinationPrefixes("/app");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                //sau nay deploy len domain that thi phai bo vao day
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS(); // endpoint cho client kết nối
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor != null ? accessor.getCommand() : null)) {
                    // Lấy token từ header
                    String token = accessor.getFirstNativeHeader("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);

                        try {
                            // Dùng bean jwtDecoder
                            Jwt jwt = jwtDecoder.decode(token);

                            // Lấy subject (username) từ token
                            String username = jwt.getSubject();

                            log.info("username " + username);



                            // Lấy user từ DB
                            User user = userRepository.findByUsername(username)
                                    .orElseThrow(() -> new RuntimeException("User not found because wweb socket"));
                            CustomUserDetails customUser = new CustomUserDetails(user.getId(), user.getUsername());
                            // Tạo Authentication
                            Authentication auth = new UsernamePasswordAuthenticationToken(
                                    customUser,
                                    null,
                                    customUser.getAuthorities()
                            );

                            accessor.setUser(auth);
                            log.info("accessor" + accessor);

                        } catch (Exception e) {
                            throw new RuntimeException("invalid jwt token ",e);
                        }
                    }
                }
                return message;
            }
        });
    }
}
