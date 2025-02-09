package com.moyoprototype.user.controller;

import com.moyoprototype.oauth.GithubOAuth2User;
import com.moyoprototype.user.service.JwtReIssueService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.moyoprototype.common.constant.MoyoPrototypeConstants.GITHUB_REGISTRATION_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final JwtReIssueService jwtReIssueService;

    @GetMapping("/auth/test")
    public String test(){

        log.info("===== default OAuth2AuthorizedClientRepository 구현체 조회 =====");
        log.info(String.valueOf(oAuth2AuthorizedClientRepository.getClass()));

        log.info("===== default OAuth2AuthorizedClientService 구현체 조회 =====");
        log.info(String.valueOf(oAuth2AuthorizedClientService.getClass()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("===== SecurityContextHolder에서 Authentication.getPrincipal 조회 =====");
        log.info(String.valueOf(auth.getPrincipal().getClass()));

        GithubOAuth2User userPrincipal = (GithubOAuth2User) auth.getPrincipal();
        log.info("===== SecurityContextHolder에서 Authentication.getPrincipal에 넣어둔 GithubOAuth2User 조회 =====");

        log.info("===== usernmae 조회 : {} =====", userPrincipal.getUsername());
        log.info("===== userProfileImg 조회 : {} =====", userPrincipal.getProfileImgUrl());



        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(GITHUB_REGISTRATION_ID, userPrincipal.getName());
        log.info("===== OAuth2AuthorizedClient 조회 : {}  =====", oAuth2AuthorizedClient);

        log.info("===== 현재 로그인 한 사용자의 OAuth2 Access Token 조회 : {} =====", oAuth2AuthorizedClient.getAccessToken().getTokenValue());

        return "OK";
    }

    @PostMapping("/reissue/token")
    public ResponseEntity<Void> reissueJwtTokens(@CookieValue("jwtRefresh") String jwtRefreshToken){

        Map<String, String> reIssueTokens = jwtReIssueService.reIssueJwt(jwtRefreshToken);

        String accessToken = reIssueTokens.get("access");
        String refreshToken = reIssueTokens.get("refresh");

        return ResponseEntity.status(200)
                .header("Authorization", "Bearer " + accessToken)
                .header("Set-Cookie","jwtRefresh=" + refreshToken + "; HttpOnly; Path=/; Max-Age=600")
                .build();
    }

}
