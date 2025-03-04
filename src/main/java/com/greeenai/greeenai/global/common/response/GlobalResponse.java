package com.greeenai.greeenai.global.common.response;

import com.greeenai.greeenai.global.error.ErrorResponse;

import java.time.LocalDateTime;

public record GlobalResponse(boolean isSuccess, int status, Object data, LocalDateTime timestamp) {
	public static GlobalResponse ok(int status, Object data) {
		return new GlobalResponse(true, status, data, LocalDateTime.now());
	}

	public static GlobalResponse error(int status, ErrorResponse errorResponse) {
		return new GlobalResponse(false, status, errorResponse, LocalDateTime.now());
	}
}
