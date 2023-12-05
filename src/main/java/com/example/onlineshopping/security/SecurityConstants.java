package com.example.onlineshopping.security;

public class SecurityConstants {
    public static final String AUTH_URLS = "/api/auth/**";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
    public static final long EXPIRATION_TIME = 1000 * 60 * 60;  // 1 hour
}
