package com.lec.spring.service.user;

import com.lec.spring.domain.user.User;
import com.lec.spring.repository.user.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    public UserServiceImpl(SqlSession sqlSession){
        userRepository = sqlSession.getMapper(UserRepository.class);
        //System.out.println(getClass().getName() + "() 생성");
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User findByName(String name){return userRepository.findByName(name);}
    @Override
    public boolean isExist(String username) {
        //System.out.println(username);
        User user = findByUsername(username);
        //System.out.println(user);
        return (user != null) ? true : false;
    }

    @Override
    public boolean isExistName(String name) {
        //System.out.println(name);
        User user = findByName(name);
        //System.out.println(user);
        return (user != null) ? true : false;
    }

    @Override
    public int register(User user) {
        // DB 에는 회원 username 을 대문자로 저장
        user.setUsername(user.getUsername().toUpperCase());

        // password 는 암호화 해서 저장.  PasswordEncoder 객체 사용
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);  // 새로운 회원 (User) 저장.  id 값 받아옴.

        return 1;
    }
}
