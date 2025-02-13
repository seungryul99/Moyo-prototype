package com.moyoprototype.oauth;

import com.moyoprototype.common.redis.repository.RedisRepository;
import com.moyoprototype.jwt.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.moyoprototype.common.constant.MoyoPrototypeConstants.JWT_REFRESH_EXPIRES_MS;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RedisRepository redisRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("로그인 성공 후 JWT 발급 시작");
        GithubOAuth2User gitHubOAuth2User = (GithubOAuth2User) authentication.getPrincipal();

        String userAppId = gitHubOAuth2User.getAppId();

        String jwtRefresh = jwtProvider.createJwtRefresh(userAppId);

        Cookie jwtRefreshCookie = new Cookie("jwtRefresh",jwtRefresh);
        jwtRefreshCookie.setMaxAge(JWT_REFRESH_EXPIRES_MS/1000);
        jwtRefreshCookie.setPath("/");
        jwtRefreshCookie.setHttpOnly(true);
        jwtRefreshCookie.setSecure(true);
        jwtRefreshCookie.setDomain("cafehub.site");

        redisRepository.save(gitHubOAuth2User.getAppId(), jwtRefresh);

        response.addCookie(jwtRefreshCookie);
        response.sendRedirect("http://localhost:3000/test");
    }

}