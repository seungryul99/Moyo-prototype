package com.moyoprototype.follow.infrastructure;

import com.moyoprototype.follow.infrastructure.dto.GithubUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowHttpClient {

    private final RestClient restClient;

    public List<GithubUserResponse> getFollowingList(String accessToken){

        return restClient.get()
                .uri("https://api.github.com/user/following")
                .headers(
                        header ->{
                            header.setBearerAuth(accessToken);
                            header.set(HttpHeaders.ACCEPT, "application/vnd.github+json");
                            header.set("X-GitHub-Api-Version", "2022-11-28");                        }
                )
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }


    public List<GithubUserResponse> getFollowerList(String accessToken) {

        return restClient.get()
                .uri("https://api.github.com/user/followers")
                .headers(
                        header ->{
                            header.setBearerAuth(accessToken);
                            header.set(HttpHeaders.ACCEPT, "application/vnd.github+json");
                            header.set("X-GitHub-Api-Version", "2022-11-28");                        }
                )
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public int follow(String username, String accessToken){
        return restClient.put()
                .uri("https://api.github.com/user/following/"+username)
                .headers(
                        header ->{
                            header.setBearerAuth(accessToken);
                            header.set(HttpHeaders.ACCEPT, "application/vnd.github+json");
                            header.set("X-GitHub-Api-Version", "2022-11-28");                        }
                )
                .retrieve()
                .toBodilessEntity()
                .getStatusCode().value();
    }

    public int unfollow(String username, String accessToken){
        return restClient.delete()
                .uri("https://api.github.com/user/following/"+username)
                .headers(
                        header ->{
                            header.setBearerAuth(accessToken);
                            header.set(HttpHeaders.ACCEPT, "application/vnd.github+json");
                            header.set("X-GitHub-Api-Version", "2022-11-28");                        }
                )
                .retrieve()
                .toBodilessEntity()
                .getStatusCode().value();
    }
}
