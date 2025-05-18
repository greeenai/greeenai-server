package com.greeenai.greeenai.domain.ai.dto.response;

import com.greeenai.greeenai.domain.image.domain.ContentType;

public record GeneratedImage(byte[] bytes, ContentType contentType) {
    public static GeneratedImage of(byte[] bytes, String contentType) {
        return new GeneratedImage(bytes, ContentType.from(contentType));
    }
}
