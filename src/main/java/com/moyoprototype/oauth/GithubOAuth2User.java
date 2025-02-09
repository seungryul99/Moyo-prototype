package com.moyoprototype.oauth;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class GithubOAuth2User implements OAuth2User{

    private final Map<String, Object> attributes;

    public GithubOAuth2User(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 식별자임
    @Override
    public String getName() {
        return getAppId();
    }

    public String getUsername(){
        return (String) attributes.get("name");
    }

    public String getAppId(){
        return (String) attributes.get("appId");
    }

    public String getProfileImgUrl(){
        return (String) attributes.get("profileImgUrl");
    }
}
