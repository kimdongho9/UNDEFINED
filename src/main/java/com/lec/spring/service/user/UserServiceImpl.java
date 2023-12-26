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
        user.setUsername(user.getUsername().toUpperCase());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return 1;
    }

    @Override
    public int update(User user) {
        user.setUsername(user.getUsername().toUpperCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.update(user);
    }
}
