package com.moyoprototype.common.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/*").permitAll()
                        .anyRequest().authenticated()
                )


                /**
                 *    [초기 구현 계획]
                 *    무조건 단일 서버 기준 세션사용 로그인으로 구현할 생각임.
                 *
                 *    1. OAuth2Login API 사용
                 *    2. API 서버의 클라이언트(프론트)에게 노출되는 로그인 버튼 클릭시 uri만 커스텀 할 생각임. code 전달되는 경로는 커스터마이징 하지 않음
                 *    3. Access 교환시 현재 버전의 시큐리티가 RestTemplate를 사용하는지 RestClient를 사용하는지 관찰 후 RestTemplate를 사용하면 이를 커스터마이징 할 생각
                 *    4. 우선은 OIDC없이 구현할 예정 이라 UserService만 커스터마이징 할 예정임.
                 *    5. 우선은 failHandler는 구현하지 않음.
                 */


                .oauth2Login(oauth2 -> oauth2

                        // 인증 후, 사용자의 OAuth2 클라이언트(예: 액세스 토큰, 리프레시 토큰 등)를 저장하고 관리
                        // 기본 구현체 : HttpSessionOAuth2AuthorizedClientRepository
                        // 백엔드 서버가 2대 이상인 환경에서 각 서버의 세션에 OAuth2 클라이언트를 저장해버리면 다중서버에서 세션 불일치 문제가 발생
                        // 따라서 우리 프로젝트에서는 커스터 마이징 포인트임.
//                        .authorizedClientRepository(this.authorizedClientRepository())

                        // 위의 repository를 이용하는 서비스임. repository를 직접 구현했으니 얘도 당연히 구현해야 함.
//                        .authorizedClientService(this.authorizedClientService())

                        .authorizationEndpoint(authorization -> authorization
//
//                                // 사용자가 로그인 하기 버튼을 누르면 해당 경로로 들어와서 LoginRedirectFilter 동작 커스텀, registrationId 전까지가 Uri임 /github 안붙여야됨
                                .baseUri("/users/login")
//
//                                // 보통 csrf 공격 방어등을 위해서 요청을 서버 세션에 저장해두고 state = 랜덤 UUID 같은 걸 붙여서 관리함
//                                // 추후 요청이 돌아오면 해당 state 값과 비교하는데 여기서도 다중 서버 세션 불일치 문제가 발생할 수 있음.
//                                // 따라서 Redis에 중앙 집중화 시키는 세션 storage 방식등을 직접 구현해야함
////                                .authorizationRequestRepository(this.authorizationRequestRepository())
//
//                                // OAuth2AuthorizationRequestRepository는 인증 요청을 저장하고,
//                                // OAuth2AuthorizationRequestResolver는 인증 요청을 해석하고 복원하는 역할
//                                // OAuth2AuthorizationRequestRepository 이걸 커스텀 했으니 Resolver도 커스텀
////                                .authorizationRequestResolver(this.authorizationRequestResolver())
                        )


//                        .redirectionEndpoint(redirection -> redirection
//                                .baseUri()
//                        )


                        // Access Token 교환을 담당하는 클라이언트를 커스터 마이징
                        // ex) 기존의 RestOperations에서 RestTemplate를 사용하는 방식이 마음에 안든다? 
                        // 내가 직접 만들어서 RestClient로 교체 가능, 하지만 이미 RestClientAuthorizationCodeTokenResponseClient라는 걸 시큐리티에서 만들어서
                        // 적용했음.
//                        .tokenEndpoint(token -> token
//                                .accessTokenResponseClient(this.accessTokenResponseClient())
//                        )


                        .userInfoEndpoint(userInfo -> userInfo

                                   // 예시: OAuth2 공급자가 ROLE_USER 대신 user라는 문자열을 전달할 경우,
                                   // 이를 ROLE_USER로 변환하는 로직을 구현할 수 있음.
//                                .userAuthoritiesMapper(this.userAuthoritiesMapper())
                                   // 사용자가 정보 받아온 후 실행할 서비스
                                .userService()
//                                .oidcUserService(this.oidcUserService())
                        )
                                .successHandler()
                );
        return http.build();
    }
}
