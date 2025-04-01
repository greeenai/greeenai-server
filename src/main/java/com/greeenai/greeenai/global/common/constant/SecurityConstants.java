package com.greeenai.greeenai.global.common.constant;

import java.util.List;

public class SecurityConstants {

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";

    public static final List<String> TOKEN_TYPES = List.of(ACCESS_TOKEN, REFRESH_TOKEN);

    private SecurityConstants() {}
}
