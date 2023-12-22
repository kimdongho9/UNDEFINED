package com.lec.spring.config;


import com.lec.spring.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) //api를 못받아옴
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .defaultSuccessUrl("/")
                        .successHandler(new CustomLoginSuccessHandler("/"))
                        .failureHandler(new CustomLoginFailureHandler())
                )
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/user/login") // 로그인 페이지는 기존과 동일한 url 로 지정

                        // code 를 받아오는 것이 아니라, AccessToken 과 사용자 profile 정보를 받아오게 된다.
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                // userService(OAuth2UserService<OAuth2UserRequest, OAuth2User>)
                                //   이 설정을 통해 인증서버의 UserInfo Endpoint 후처리 진행
                                .userService(principalOauth2UserService)
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                )
                .build();


    }

    // OAuth 로그인
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}//end filter
