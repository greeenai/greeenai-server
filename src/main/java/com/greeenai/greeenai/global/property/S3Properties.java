package com.greeenai.greeenai.global.property;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Properties {

    private final S3 s3;
    private final String region;
    private final Credentials credentials;

    @ConstructorBinding
    public S3Properties(Credentials credentials, S3 s3, Map<String, String> region) {
        this.credentials = credentials;
        this.s3 = s3;
        this.region = region.get("static");
    }

    public record S3(String bucket) {}

    public record Credentials(String accessKey, String secretKey) {}
}
