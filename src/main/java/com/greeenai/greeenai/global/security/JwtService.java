package com.greeenai.greeenai.global.security;

import static com.greeenai.greeenai.global.common.constant.SecurityConstants.*;
import static com.greeenai.greeenai.global.error.exception.ErrorCode.TOKEN_INVALID;

import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.property.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private Key accessTokenSecretKey;
    private Key refreshTokenSecretKey;

    @PostConstruct
    protected void init() {
        accessTokenSecretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(getTokenProperty(ACCESS_TOKEN).secret()));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(getTokenProperty(REFRESH_TOKEN).secret()));
    }

    public Long parseToken(String tokenType, String token) {
        validateToken(token);

        Jws<Claims> claims = Jwts.parserBuilder()
                .requireIssuer(jwtProperties.getIssuer())
                .setSigningKey(getSecretKey(tokenType))
                .build()
                .parseClaimsJws(token);

        return claims.getBody().get("memberId", Long.class);
    }

    public String generateToken(String tokenType, Long memberId) {
        Date now = new Date();
        Date expirationDate =
                new Date(now.getTime() + getTokenProperty(tokenType).expirationMilliTime());
        return Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(memberId.toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSecretKey(tokenType))
                .compact();
    }

    private void validateToken(String token) {
        if (token == null) {
            throw new CustomException(TOKEN_INVALID);
        }
    }

    private Key getSecretKey(String tokenType) {
        if (ACCESS_TOKEN.equals(tokenType)) {
            return accessTokenSecretKey;
        } else {
            return refreshTokenSecretKey;
        }
    }

    private JwtProperties.TokenProperty getTokenProperty(String tokenType) {
        return jwtProperties.getToken().get(tokenType);
    }
}
