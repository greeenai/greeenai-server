package com.greeenai.greeenai.domain.image.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileExtension {
    JPEG("jpeg"),
    ;

    private final String value;
}
