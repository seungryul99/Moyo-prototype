package com.moyoprototype.follow.application;

import com.moyoprototype.follow.infrastructure.GithubHttpClient;
import com.moyoprototype.follow.infrastructure.dto.GithubUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


// 테스트 모듈이라 객체지향, 유지보수 성능 이런거 고려 x
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final GithubHttpClient githubClient;

    // 나만 팔로잉 = 나의 팔로잉 목록 - 나의 팔로워 목록
    public Set<GithubUserResponse> getFollowingOnlyList(String accessToken) {

        Set<GithubUserResponse> followingSet = new HashSet<>(githubClient.getFollowingList(accessToken));
        Set<GithubUserResponse> followerSet = new HashSet<>(githubClient.getFollowerList(accessToken));
        followingSet.removeAll(followerSet);

        return followingSet;
    }

    // 상대만 나를 팔로잉 = 나의 팔로워 목록 - 나의 팔로잉 목록
    public Set<GithubUserResponse> getFollowerOnlyList(String accessToken) {

        Set<GithubUserResponse> followerSet = new HashSet<>(githubClient.getFollowerList(accessToken));
        Set<GithubUserResponse> followingSet = new HashSet<>(githubClient.getFollowingList(accessToken));
        followerSet.removeAll(followingSet);

        return followerSet;
    }

    public Set<GithubUserResponse> getMutualFollowList(String accessToken){

        Set<GithubUserResponse> followingSet = new HashSet<>(githubClient.getFollowingList(accessToken));
        Set<GithubUserResponse> followerSet = new HashSet<>(githubClient.getFollowerList(accessToken));

        followingSet.retainAll(followerSet);

        return followingSet;
    }

    public void follow(String username, String accessToken) {

        if(githubClient.follow(username,accessToken)!=204) {
            log.info("깃허브 팔로우 요청 에러 발생");
        }
    }

    public void unfollow(String username, String accessToken) {

        if(githubClient.unfollow(username,accessToken)!=204) {
            log.info("깃허브 언 팔로우 요청 에러 발생");
        }
    }
}
