package com.greeenai.greeenai.infra.ai.dto.response;

public record GeneratedImage(byte[] bytes, String contentType) {
    public static GeneratedImage of(byte[] bytes, String contentType) {
        return new GeneratedImage(bytes, contentType);
    }
}
