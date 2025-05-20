package com.greeenai.greeenai.global.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "custom.webclient")
public class WebClientProperties {

	private final String kaggomBaseUrl;
	private final String aiBaseUrl;
}
