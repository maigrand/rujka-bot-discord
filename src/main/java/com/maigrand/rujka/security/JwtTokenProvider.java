package com.maigrand.rujka.security;

import com.maigrand.rujka.entity.UserEntity;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.security.jwt.secret}")
    private String secret;

    @Value("${app.security.jwt.expire-length}")
    private int expireLength;

    @Value("${app.security.jwt.remember-me-expire-length}")
    private int rememberMeExpireLength;

    public String generateToken(String email, boolean rememberMe) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (rememberMe ? this.rememberMeExpireLength : this.expireLength));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, this.secret)
                .compact();
    }

    public String generateToken(UserEntity userEntity, boolean rememberMe) {
        return generateToken(userEntity.getEmail(), rememberMe);
    }

    public String generateToken(Authentication authentication, boolean rememberMe) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return generateToken(user, rememberMe);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
