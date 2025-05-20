package com.greeenai.greeenai.domain.image.domain;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.CONTENT_TYPE_INVALID;

import com.greeenai.greeenai.global.error.exception.CustomException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    JPEG("image/jpeg", "jpeg"),
    PNG("image/png", "png"),
    ;

    private final String value;
    private final String extension;

    public static ContentType from(final String contentType) {
        return Arrays.stream(ContentType.values())
                .filter(type -> type.getValue().equals(contentType))
                .findFirst()
                .orElseThrow(() -> new CustomException(CONTENT_TYPE_INVALID));
    }
}
