package com.moyoprototype.common.redis.repository;

public interface RedisRepository {
    void save(String memberAppId, String refreshToken);

    String findWhiteListTokenKey(String memberAppId, String jwtRefreshToken);

    void delete(String key);
}