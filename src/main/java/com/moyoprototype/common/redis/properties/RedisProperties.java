package com.moyoprototype.common.redis.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("spring.data.redis")
public class RedisProperties {

    private final String host;
    private final Long port;
}