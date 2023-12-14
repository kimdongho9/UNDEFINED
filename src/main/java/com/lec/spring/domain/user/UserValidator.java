package com.lec.spring.domain.user;

import com.lec.spring.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("supports(" + clazz.getName() + ")");
        // ↓ 검증할 객체의 클래스 타입인지 확인
        boolean result = User.class.isAssignableFrom(clazz);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User)target;

        String username = user.getUsername();
        if(username == null || username.trim().isEmpty()) {
            errors.rejectValue("username", "username 은 필수입니다"); // rejectValue(field, errorcode)
        } else if(userService.isExist(username)){
            // 이미 등록된 중복된 아이디(username) 이 들어오면
            errors.rejectValue("username", "이미 존재하는 아이디(username) 입니다");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name 은 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password 는 필수입니다");

    }
}
