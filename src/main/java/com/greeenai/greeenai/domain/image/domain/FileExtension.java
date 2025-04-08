package com.greeenai.greeenai.domain.image.domain;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.FILE_EXTENSION_NOT_FOUND;

import com.greeenai.greeenai.global.error.exception.CustomException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileExtension {
    JPEG("jpeg"),
    ;

    private final String value;

    public static FileExtension from(final String fileExtension) {
        return Arrays.stream(FileExtension.values())
                .filter(extension -> extension.getValue().equals(fileExtension))
                .findFirst()
                .orElseThrow(() -> new CustomException(FILE_EXTENSION_NOT_FOUND));
    }
}
