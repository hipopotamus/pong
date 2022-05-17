package com.hipo.validator;

import com.hipo.web.form.AccountUpdateInfoForm;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AccountUpdateInfoFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountUpdateInfoForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AccountUpdateInfoForm accountUpdateInfoForm = (AccountUpdateInfoForm) target;

        //nickname//
        if (accountRepository.existsByNickname(accountUpdateInfoForm.getNickname())) {
            errors.rejectValue("nickname", "NicknameDuplication");
        }
        //nickname//

        //birthDate//
        LocalDate birtDate = accountUpdateInfoForm.getBirthDate();

        if (birtDate != null && birtDate.isAfter(LocalDate.now())) {
            errors.rejectValue("birthDate", "FutureBirthDate");
        }
        //birthDate//
    }
}
