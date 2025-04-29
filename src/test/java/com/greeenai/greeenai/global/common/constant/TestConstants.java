package com.greeenai.greeenai.global.common.constant;

import com.greeenai.greeenai.domain.member.domain.OauthProvider;

public class TestConstants {

    public static final Long TEST_MEMBER_ID = 1L;
    public static final String TEST_NAME = "testName";
    public static final String TEST_EMAIL = "test@email.com";
    public static final String TEST_OAUTH_ID = "testOauthId";
    public static final OauthProvider TEST_OAUTH_PROVIDER = OauthProvider.APPLE;

    public static final String TEST_ACCESS_TOKEN = "testAccessToken";
    public static final String TEST_REFRESH_TOKEN = "testRefreshToken";
    public static final String TEST_SECRET = "testSecretKeyWithAtLeast32Characters";
    public static final long TEST_EXPIRATION_TIME = 3600L;

    private TestConstants() {}
}
