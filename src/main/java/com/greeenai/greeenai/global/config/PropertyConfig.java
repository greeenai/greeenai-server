package com.greeenai.greeenai.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.greeenai.greeenai.global.property.JwtProperties;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class PropertyConfig {
}
