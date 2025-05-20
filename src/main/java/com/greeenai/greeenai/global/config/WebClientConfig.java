package com.greeenai.greeenai.global.config;

import static com.greeenai.greeenai.global.common.constant.WebClientConstants.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;

import com.greeenai.greeenai.global.property.WebClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

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
                        .codecs(config -> config.defaultCodecs().maxInMemorySize(MAX_RESPONSE_BODY_SIZE))
                        .build())
                .build();
    }
}
