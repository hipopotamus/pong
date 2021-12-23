package com.hipo.properties;

public interface JwtProperties {
    String SECRET = "hipopotamus";
    int EXPIRATION_TIME = 60000 * 60;
    String TOKEN_PREFIX = "Bearer";
    String HEADER_STRING = "Authorization";
}
