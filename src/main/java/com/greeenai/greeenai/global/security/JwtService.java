package com.greeenai.greeenai.global.security;

import static com.greeenai.greeenai.global.common.SecurityConstants.*;

import com.greeenai.greeenai.global.property.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
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
        accessTokenSecretKey =
                Keys.hmacShaKeyFor(getTokenProperty(ACCESS_TOKEN).secret().getBytes());
        refreshTokenSecretKey =
                Keys.hmacShaKeyFor(getTokenProperty(REFRESH_TOKEN).secret().getBytes());
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
            // todo: throw custom exception
            throw new RuntimeException("Token is null");
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
