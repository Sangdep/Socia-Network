package com.sangle.Network.Social.Configuration;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerkey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(request ->
                    request
                            .requestMatchers("/users/register","/auth/login","/uploads/**").permitAll()
                            .anyRequest().authenticated()
                        );

        //xác thực( authentication)
        http.oauth2ResourceServer(oauth2->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder()) //kiểm tra token có hợp lệ hay k , sau đó gọi tới để giải
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))

        );
        // Tắt CSRF nếu cần
        http.csrf(AbstractHttpConfigurer::disable);


        return http.build();



    }

    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration corsConfiguration= new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:3000/chat"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource= new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }




    //customer lai SCOPE_ thanh ROLE_
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter()
    {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter= new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");//custom Scope thành ROLE

        JwtAuthenticationConverter jwtAuthenticationConverter= new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }

    @Bean
    JwtDecoder jwtDecoder()
    {
        //giải mã jwt
        SecretKeySpec secretKeySpec= new SecretKeySpec(signerkey.getBytes(),"HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder ()
    {
        return new BCryptPasswordEncoder(10);
    }

}
