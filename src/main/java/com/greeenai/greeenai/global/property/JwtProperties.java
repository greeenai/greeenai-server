package com.greeenai.greeenai.global.property;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private final Map<String, TokenProperty> token;
	private final String issuer;

	public record TokenProperty(String secret, Long expirationTime) {
		public Long expirationMilliTime() {
			return expirationTime * 1000;
		}
	}
}
