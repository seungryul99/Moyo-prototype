package com.moyoprototype.follow.ui;

import com.moyoprototype.common.model.ApiResponse;
import com.moyoprototype.follow.application.FollowService;
import com.moyoprototype.oauth.GithubOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.bind.annotation.*;

import static com.moyoprototype.common.constant.MoyoPrototypeConstants.GITHUB_REGISTRATION_ID;

@Slf4j
@RequestMapping("/api/auth/follow")
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;


    // 나만 팔로잉
    @GetMapping("/following-only")
    public ResponseEntity<?> following(){

        GithubOAuth2User userPrincipal = (GithubOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String accessToken = oAuth2AuthorizedClientService.loadAuthorizedClient(GITHUB_REGISTRATION_ID, userPrincipal.getName()).getAccessToken().getTokenValue();
        log.info("나만 팔로잉 하는 사람 찾아보기 요청 발생시킴 요청자 : {}", userPrincipal.getUsername());

        return ResponseEntity.ok(ApiResponse.success(followService.getFollowingOnlyList(accessToken)));
    }


    // 상대만 나를 팔로잉
    @GetMapping("/follower-only")
    public ResponseEntity<?> follower(){

        GithubOAuth2User userPrincipal = (GithubOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String accessToken = oAuth2AuthorizedClientService.loadAuthorizedClient(GITHUB_REGISTRATION_ID, userPrincipal.getName()).getAccessToken().getTokenValue();
        log.info("상대만 나를 팔로우 하는 사람 찾아보기 요청자 : {}", userPrincipal.getUsername());

        return ResponseEntity.ok(ApiResponse.success(followService.getFollowerOnlyList(accessToken)));
    }

    // 서로 팔로잉 = 나의 팔로잉 목록 ∩ 나의 팔로워 목록
    @GetMapping("/mutual")
    public ResponseEntity<?> mutualFollowingDetect(){

        GithubOAuth2User userPrincipal = (GithubOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String accessToken = oAuth2AuthorizedClientService.loadAuthorizedClient(GITHUB_REGISTRATION_ID, userPrincipal.getName()).getAccessToken().getTokenValue();
        log.info("맞팔로우 리스트 조회, 요청자 : {}", userPrincipal.getUsername());

        return ResponseEntity.ok(ApiResponse.success(followService.getMutualFollowList(accessToken)));
    }

    @PostMapping("/{username}")
    public ResponseEntity<?> follow(@PathVariable("username") String username){

        GithubOAuth2User userPrincipal = (GithubOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String accessToken = oAuth2AuthorizedClientService.loadAuthorizedClient(GITHUB_REGISTRATION_ID, userPrincipal.getName()).getAccessToken().getTokenValue();
        log.info("팔로우 요청, 요청자 : {}", userPrincipal.getUsername());

        followService.follow(username,accessToken);

        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> unfollow(@PathVariable("username") String username){

        GithubOAuth2User userPrincipal = (GithubOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String accessToken = oAuth2AuthorizedClientService.loadAuthorizedClient(GITHUB_REGISTRATION_ID, userPrincipal.getName()).getAccessToken().getTokenValue();
        log.info("언 팔로우 요청, 요청자 : {}", userPrincipal.getUsername());

        followService.unfollow(username,accessToken);

        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
