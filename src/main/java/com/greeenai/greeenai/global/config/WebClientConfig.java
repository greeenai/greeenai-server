package com.greeenai.greeenai.global.config;

import com.greeenai.greeenai.global.property.WebClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;

@RequiredArgsConstructor
@Configuration
public class WebClientConfig {

    private final WebClientProperties webClientProperties;

    @Bean
    public WebClient kaggomWebClient(WebClient.Builder builder) {
        return builder.baseUrl(webClientProperties.getKaggomBaseUrl())
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient aiWebClient(WebClient.Builder builder) {
        return builder.baseUrl(webClientProperties.getAiBaseUrl())
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(config -> config.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB로 확장
                        .build())
                .build();
    }
}
