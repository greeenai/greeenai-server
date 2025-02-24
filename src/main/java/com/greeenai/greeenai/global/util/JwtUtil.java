package com.greeenai.greeenai.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    // header로부터 token을 추출합니다.
    public String extractTokenFromHeader(HttpServletRequest request, String tokenType) {
        return request.getHeader(tokenType);
    }

    // token을 response header에 추가합니다.
    public void addTokenToHeader(HttpServletResponse response, String tokenType, String token) {
        response.addHeader(tokenType, token);
    }
}
