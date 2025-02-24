package com.greeenai.greeenai.global.config;

import com.greeenai.greeenai.global.property.JwtProperties;
import com.greeenai.greeenai.global.property.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, RedisProperties.class})
public class PropertyConfig {}
