package com.greeenai.greeenai.global.config;

import com.greeenai.greeenai.global.property.S3Properties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@EnableConfigurationProperties({S3Properties.class})
@Import({S3Config.class})
public class TestS3Config {}
