package com.moyoprototype.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.moyoprototype.common.constant.MoyoPrototypeConstants.JWT_ACCESS_EXPIRES_MS;
import static com.moyoprototype.common.constant.MoyoPrototypeConstants.JWT_REFRESH_EXPIRES_MS;

@Component
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${spring.jwt.secret}") String jwtSecret) {
        this.secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createJwtAccess(String memberAppId) {
        return Jwts.builder()
                .claim("tokenType", "jwt_access")
                .claim("memberAppId", memberAppId)
                .claim("role", "ROLE_USER")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_ACCESS_EXPIRES_MS))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtRefresh(String memberAppId) {
        return Jwts.builder()
                .claim("tokenType", "jwt_refresh")
                .claim("memberAppId", memberAppId)
                .claim("role", "ROLE_USER")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_REFRESH_EXPIRES_MS))
                .signWith(secretKey)
                .compact();
    }

}
