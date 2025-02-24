package com.greeenai.greeenai.global.security;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final Long memberId;

    public static PrincipalDetails from(Long memberId) {
        return new PrincipalDetails(memberId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberId.toString();
    }
}
