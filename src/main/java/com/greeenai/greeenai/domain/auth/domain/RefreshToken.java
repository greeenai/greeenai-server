package com.greeenai.greeenai.domain.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken {

    @Id
    private Long memberId;

    private String value;

    @TimeToLive
    private long ttl;

    @Builder(access = AccessLevel.PRIVATE)
    private RefreshToken(Long memberId, String value, long ttl) {
        this.memberId = memberId;
        this.value = value;
        this.ttl = ttl;
    }

    public static RefreshToken create(Long memberId, String value, long ttl) {
        return RefreshToken.builder().memberId(memberId).value(value).ttl(ttl).build();
    }
}
