package com.greeenai.greeenai.domain.member.dto;

import com.greeenai.greeenai.domain.member.domain.OAuthProvider;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String name, @NotBlank String email, @NotBlank String oAuthId, @NotBlank OAuthProvider oAuthProvider) {
}
