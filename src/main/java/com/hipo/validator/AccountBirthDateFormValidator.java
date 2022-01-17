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

        if (birtDate != null && (birtDate.isAfter(LocalDate.now()) || birtDate.isEqual(LocalDate.now()))) {
            errors.rejectValue("birthDate", "FutureBirthDate");
        }
    }
}
