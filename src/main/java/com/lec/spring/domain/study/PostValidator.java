package com.lec.spring.domain.study;

import com.lec.spring.controller.community.CommunityController;
import com.lec.spring.domain.community.Feed;
import com.lec.spring.domain.user.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class PostValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("supports(" + clazz.getName() + ")");
//        boolean result = Post.class.isAssignableFrom(clazz);

        boolean result1 = Post.class.isAssignableFrom(clazz);
        boolean result2 = User.class.isAssignableFrom(clazz);
//        System.out.println(result);
        return result1 || result2;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Post post = (Post) target;

        String title = post.getTitle();
        String startdate = post.getDataForStartdate();
        String enddate = post.getDataForEnddate();
        String skills = post.getSkills();




        // 빈 문자열 또는 null 체크
        if (startdate == null || startdate.trim().isEmpty()) {
            errors.rejectValue("dataForStartdate", "시작일을 입력해야 합니다");
        }

        if (enddate == null || enddate.trim().isEmpty()) {
            errors.rejectValue("dataForEnddate", "마감일을 입력해야 합니다");
        } else {
            // 날짜 문자열을 LocalDate 객체로 변환
            LocalDate startDateObj = LocalDate.parse(startdate);
            LocalDate endDateObj = LocalDate.parse(enddate);

            // startDate가 endDate보다 빠를 때
            if (!endDateObj.isBefore(startDateObj)) {
                // 오류 처리: endDate가 startDate보다 빠르지 않은 경우
                errors.rejectValue("dataForEnddate", "마감일은 시작일보다 빨라야 합니다");
            }
        }

        if (title == null || title.trim().isEmpty()) {
            errors.rejectValue("title", "제목은 필수 입니다");
        }

        if(skills == null || skills.trim().isEmpty()){
            errors.rejectValue("skills", "스터디 언어는 필수 입니다.");
        }
    }
}
