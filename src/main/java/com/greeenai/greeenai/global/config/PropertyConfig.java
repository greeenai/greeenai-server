package com.greeenai.greeenai.global.config;

import com.greeenai.greeenai.global.property.JwtProperties;
import com.greeenai.greeenai.global.property.RedisProperties;
import com.greeenai.greeenai.global.property.S3Properties;
import com.greeenai.greeenai.global.property.WebClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, RedisProperties.class, S3Properties.class, WebClientProperties.class})
public class PropertyConfig {}
