package com.greeenai.greeenai.domain.member.dto;

import com.greeenai.greeenai.domain.member.domain.OauthProvider;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String name, @NotBlank String email, @NotBlank String oauthId, OauthProvider oAuthProvider) {}
