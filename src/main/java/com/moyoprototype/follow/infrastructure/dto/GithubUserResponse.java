package com.moyoprototype.follow.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode
public class GithubUserResponse {

    @JsonProperty("login")
    private String login;
    @JsonProperty("avatar_url")
    private String avatarUrl;


}
