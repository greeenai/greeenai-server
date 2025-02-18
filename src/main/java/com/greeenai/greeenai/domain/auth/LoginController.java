package com.greeenai.greeenai.domain.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greeenai.greeenai.domain.member.dto.LoginRequest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@PostMapping
	public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
		loginService.login(request, response);
		return ResponseEntity.ok().build();
	}
}
