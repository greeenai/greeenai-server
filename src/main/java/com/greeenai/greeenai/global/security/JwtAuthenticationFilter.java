package com.greeenai.greeenai.global.security;

import static com.greeenai.greeenai.global.common.SecurityConstants.*;

import com.greeenai.greeenai.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = jwtUtil.extractTokenFromCookie(request, ACCESS_TOKEN);
        String refreshToken = jwtUtil.extractTokenFromCookie(request, REFRESH_TOKEN);

        try {
            Long memberId = jwtService.parseToken(ACCESS_TOKEN, accessToken);
            setAuthenticationToContext(PrincipalDetails.from(memberId));
        } catch (ExpiredJwtException e) {
            Long memberId = jwtService.parseToken(REFRESH_TOKEN, refreshToken);
            String newAccessToken = jwtService.generateToken(ACCESS_TOKEN, memberId);
            jwtUtil.addTokenCookie(response, ACCESS_TOKEN, newAccessToken);
            setAuthenticationToContext(PrincipalDetails.from(memberId));
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
