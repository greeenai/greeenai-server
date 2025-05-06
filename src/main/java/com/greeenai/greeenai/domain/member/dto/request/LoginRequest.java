package com.greeenai.greeenai.domain.member.dto.request;

import com.greeenai.greeenai.domain.member.domain.OauthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String oauthId,
        @NotNull OauthProvider oAuthProvider) {}
