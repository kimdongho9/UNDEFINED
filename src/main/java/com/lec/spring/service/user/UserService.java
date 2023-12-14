package com.lec.spring.service.user;

import com.lec.spring.domain.user.User;

public interface UserService {
    User findByUsername(String username);
    // 특정 username(회원 아이디) 의 회원이 존재하는지 확인
    boolean isExist(String username);

    boolean isExistName(String name);

    // 신규 회원 등록
    int register(User user);
}
