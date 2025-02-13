package com.moyoprototype.jwt.filter;

import com.moyoprototype.jwt.util.JwtPayloadReader;
import com.moyoprototype.jwt.util.JwtValidator;
import com.moyoprototype.oauth.GithubOAuth2User;
import com.moyoprototype.oauth.GithubOAuth2UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;
    private final JwtPayloadReader jwtPayloadReader;
    private final GithubOAuth2UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestURI = request.getRequestURI();
        if (!requestURI.startsWith("/api/auth")){
            filterChain.doFilter(request,response);
            return;
        }

        log.info("JWT가 필요한 요청");

        // Request Authorization 헤더에서 "토큰타입 + 토큰" 추출
        String authorizationHeader = request.getHeader("Authorization");

        // JWT가 필요한 요청인데 Authorization Header에 아무 값이 존재하지 않는 경우 예외 처리
        if(authorizationHeader == null){
            throw new RuntimeException("Authorization 헤더가 존재하지 않는 오류 발생");
        }

        // Authorization Header가 존재하는데 토큰 타입이 Bearer가 아닌 경우 예외 처리
        if(!authorizationHeader.startsWith("Bearer")) throw new RuntimeException("Bearer 토큰이 아닌 오류 발생");

        // Authorization Header에서 실제 토큰 추출
        String accessToken = authorizationHeader.substring(7);

        // 추출한 jwt AccessToken JWT Validator로 검증
        jwtValidator.validateJwtAccessToken(accessToken);

        String userAppId = jwtPayloadReader.getUserAppId(accessToken);
        GithubOAuth2User userDetails = userService.loadGithubUser(userAppId);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Spring Security의 SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터로 넘어감
        filterChain.doFilter(request,response);
    }
}
