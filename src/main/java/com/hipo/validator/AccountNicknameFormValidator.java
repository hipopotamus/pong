package com.hipo.validator;

import com.hipo.dataobjcet.form.AccountNicknameForm;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountNicknameFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountNicknameForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountNicknameForm accountNicknameForm = (AccountNicknameForm) target;

        //** nickname
        if (accountRepository.existsByNickname(accountNicknameForm.getNickname())) {
            errors.rejectValue("nickname", "NicknameDuplication");
        }
    }
}
