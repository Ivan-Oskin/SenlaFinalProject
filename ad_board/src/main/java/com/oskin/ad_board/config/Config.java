package com.oskin.ad_board.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private Long lifetime;

    public Config() {
    }

    public String getSecret() {
        return secret;
    }

    public Long getLifetime() {
        return lifetime;
    }

}
