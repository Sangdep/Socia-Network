package com.sangle.Network.Social.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.sangle.Network.Social.DTO.Request.LoginRequest;
import com.sangle.Network.Social.DTO.Response.LoginResponse;
import com.sangle.Network.Social.Entity.User;
import com.sangle.Network.Social.Exception.AppException;
import com.sangle.Network.Social.Exception.ErorrCode;
import com.sangle.Network.Social.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class AuthService {

    UserRepository userRepository;

    //vao encrytion key generator
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;


    //Đăng Nhập
    public LoginResponse login(LoginRequest request)
    {

        var user=userRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new AppException(ErorrCode.USER_NOT_FOUND));

        //check password tu request voi password repo
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);
        boolean authenticated= passwordEncoder.matches(request.getPassword(),user.getPassword());
        if (!authenticated)
        {
            throw new AppException(ErorrCode.UNAUTHENTICATED);
        }
        var token= genereToken(user);
        return LoginResponse.builder()
                .token(token)
                .Authenticated(true)
                .role(buildScope(user))
                .build();
    }
    private String genereToken(User user)
    {
        //tao header
        JWSHeader header= new JWSHeader(JWSAlgorithm.HS512);
        //tao claim
        JWTClaimsSet jwtClaimsSet=new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("sangledev.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(5    , ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();
        //chuyen claim vao Payload
        Payload payload =new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject= new JWSObject(header,payload);

        //ky token va chuyen chuoi thanh JWT hoan chinh

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();

        } catch (JOSEException e) {
            log.error("can't create token");
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user)
    {
        StringJoiner stringJoiner=new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRole()))
        {
            user.getRole().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }


}
