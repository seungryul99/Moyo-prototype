package com.moyoprototype.oauth;

import com.moyoprototype.user.domain.User;
import com.moyoprototype.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.moyoprototype.common.constant.MoyoPrototypeConstants.GITHUB_REGISTRATION_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public GithubOAuth2User loadGithubUser(String userAppId){

        User user = userRepository.findByAppId(userAppId);

        Map<String, Object> attributes = Map.of("name",user.getName(),"appId",user.getAppId(),"profileImgUrl",user.getProfileImgUrl());

        return new GithubOAuth2User(attributes);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 체크용
        // Map<String, Object> oAuth2UserAttribute = checkDefaultOAuth2UserAttribute(userRequest, oAuth2User);
        Map<String, Object> githubUserAttribute = convertAndCheckGithubUser(oAuth2User.getAttributes());

        User user = userRepository.findByAppId((String) githubUserAttribute.get("appId"));

        // 신규 회원 이라면 DB에 데이터 저장.
        if (user==null){
            log.info("신규 회원 회원 가입 처리 진행");

            User newUser = User.builder()
                    .appId((String) githubUserAttribute.get("appId"))
                    .name((String) githubUserAttribute.get("name"))
                    .profileImgUrl((String) githubUserAttribute.get("profileImgUrl"))
                    .build();

            userRepository.save(newUser);

            log.info("회원가입 완료");
        }
        else log.info("이미 가입된 회원 입니다.");

        return new GithubOAuth2User(githubUserAttribute);
    }

    private Map<String, Object> checkDefaultOAuth2UserAttribute(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        log.info("===================== OAuth2User 체크 시작 =====================");
        log.info("oAuth2User: {}", oAuth2User);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        attributes.forEach((key, value) -> {
            log.info("Key: {}, Value: {}", key, value);
        });
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId : {}", registrationId);
        log.info("registrationId == github ? : {}",registrationId.equals(GITHUB_REGISTRATION_ID));
        log.info("===================== OAuth2User 체크 완료 =====================");
        return attributes;
    }

    private Map<String, Object> convertAndCheckGithubUser(Map<String, Object> oAuth2UserAttribute){
        log.info("===================== GithubUser 체크 시작 =====================");
        String appId = String.valueOf(oAuth2UserAttribute.get("id"));
        log.info("oAuthAppId : {}", appId);

        String profileImgUrl = (String) oAuth2UserAttribute.get("avatar_url");
        log.info("profileImgUrl : {}", profileImgUrl);

        String userTag = (String) oAuth2UserAttribute.get("login");
        log.info("userTag : {}", userTag);
        log.info("===================== GithubUser 체크 완료 =====================");

        return Map.of("appId",appId,"profileImgUrl",profileImgUrl,"name",userTag);
    }
    

}
