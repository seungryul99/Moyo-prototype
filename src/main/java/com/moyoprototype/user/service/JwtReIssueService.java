package com.moyoprototype.user.service;

import com.moyoprototype.common.redis.repository.RedisRepository;
import com.moyoprototype.jwt.util.JwtPayloadReader;
import com.moyoprototype.jwt.util.JwtProvider;
import com.moyoprototype.jwt.util.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtReIssueService {

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final JwtPayloadReader jwtPayloadReader;
    private final RedisRepository redisRepository;
    public Map<String, String> reIssueJwt(String jwtRefreshToken) {

        // 리프레시 토큰 검증
        jwtValidator.validateJwtRefreshToken(jwtRefreshToken);

        // 화이트리스트에 토큰이 없다면 차단 처리
        String userAppId = jwtPayloadReader.getUserAppId(jwtRefreshToken);
        String whiteListTokenKey = redisRepository.findWhiteListTokenKey(userAppId, jwtRefreshToken);
        if(whiteListTokenKey==null) throw new RuntimeException("차단된 리프레시토큰 입니다.");

        // 유효한 토큰이고 화이트 리스트에 등록된 토큰이라면 기존의 refresh를 이용해 jwtaccess, jwtrefresh 재발급
        String newAccess = jwtProvider.createJwtAccess(userAppId);
        String newRefresh = jwtProvider.createJwtRefresh(userAppId);

        // RTR
        redisRepository.delete(whiteListTokenKey);
        redisRepository.save(userAppId,newRefresh);

        return Map.of("access", newAccess,"refresh", newRefresh);
    }
}
