package com.greeenai.greeenai.global.config;

import com.greeenai.greeenai.global.property.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class PropertyConfig {}
