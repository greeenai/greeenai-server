package com.greeenai.greeenai.domain.diary.dto.response;

public record DownloadUrlResponse(String downloadUrl) {
    public static DownloadUrlResponse from(String downloadUrl) {
        return new DownloadUrlResponse(downloadUrl);
    }
}
