package com.hipo.validator;

import com.hipo.dataobjcet.form.AccountBirthDateForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class AccountBirthDateFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountBirthDateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AccountBirthDateForm accountBirthDateForm = (AccountBirthDateForm) target;

        LocalDate birtDate = accountBirthDateForm.getBirthDate();

        if (birtDate.isAfter(LocalDate.now()) || birtDate.isEqual(LocalDate.now())) {
            errors.reject("IllegalBirthDate", "생년월일이 당일과 같거나 빠를 수 없습니다.");
        }
    }
}
