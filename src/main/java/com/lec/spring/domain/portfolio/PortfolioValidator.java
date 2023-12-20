package com.lec.spring.domain.portfolio;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PortfolioValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        boolean result = Portfolio.class.equals(clazz);
        System.out.println(result);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Portfolio portfolio = (Portfolio) target;

        // 제목이 비어 있는지 확인하고 필요에 따라 에러 메시지 추가
        if (portfolio.getTitle() == null || portfolio.getTitle().trim().isEmpty()) {
            errors.rejectValue("title", "제목을 입력해주세요." );
        } else if (portfolio.getTitle().length() > 75) {
            errors.rejectValue("title", "제목은 최대 75자까지 입력할 수 있습니다." );
        }

        // 내용이 비어 있는지 확인하고 필요에 따라 에러 메시지 추가
        System.out.println("validation content : " + portfolio.getContent());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "내용은 필수입니다." );

        // 경력(experience)이 비어 있는지 확인하고 필요에 따라 에러 메시지 추가
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "experience", "경력은 필수입니다.");

        // 자기 소개(userPr)가 비어 있는지 확인하고 필요에 따라 에러 메시지 추가
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userPr", "자기 소개는 필수입니다." );

        if (portfolio.getExperience().length() > 200) {
            errors.rejectValue("experience", "경력은 최대 200자까지 입력할 수 있습니다.");
        }

        if (portfolio.getUserPr().length() > 300) {
            errors.rejectValue("userPr", "자기 소개는 최대 300자까지 입력할 수 있습니다.");
        }
    }
}
