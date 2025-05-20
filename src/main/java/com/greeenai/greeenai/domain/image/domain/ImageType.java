package com.greeenai.greeenai.domain.image.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {
    DIARY("diary"),
    USER("user"),
    ;

    private final String value;
}
