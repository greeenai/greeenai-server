package com.greeenai.greeenai.infra.cloudfront;

import com.greeenai.greeenai.global.property.S3Properties;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class CloudFrontUrlGenerator {
    private static String cloudFrontDomain;
    private final S3Properties s3Properties;

    public CloudFrontUrlGenerator(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
    }

    @PostConstruct
    private void init() {
        cloudFrontDomain = s3Properties.getCloudFront().domain();
    }

    public static String generateUrlByFileName(final String fileName) {
        return cloudFrontDomain + "/" + fileName;
    }
}
