package com.lec.spring.config;


import com.lec.spring.domain.user.User;
import com.lec.spring.service.user.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // 로그인한 사용자 정보
    private User user;

    public User getUser() {
        return user;
    }

    // 일반 로그인 용 생성자
    public PrincipalDetails(User user){
        //System.out.println("UserDetails(user) 생성: " + user);
        this.user = user;
    }

    // OAuth 로그인용 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;    // 이때 User 정보는, 인증 직후 provider 로부터 받은 attributes 를 토대로 생성하게 된다.
        this.attributes = attributes;
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠긴건 아닌지?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 credential 이 만료된건 아닌지?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 활성화 되었는지?
    @Override
    public boolean isEnabled() {
        return true;
    }

    //-------------------------------------------------------------
    // OAuth2User 를 implement 하면 구현할 메소드

    private Map<String, Object> attributes;     // ← OAuth2User 의 getAttributes() 값

    @Override
    public String getName() {
        return null;   // 사용하지 않을 예정
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;  // 어디서 받아올까?  -> 생성자!
    }
}








