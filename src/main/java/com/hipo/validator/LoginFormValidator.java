package com.hipo.validator;

import com.hipo.web.form.LoginForm;
import com.hipo.domain.entity.Account;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class LoginFormValidator implements Validator {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(LoginForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        LoginForm loginForm = (LoginForm) target;

        //** username
        Account account = accountRepository.findByUsername(loginForm.getUsername())
                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));

        //** password
        if (!bCryptPasswordEncoder.matches(loginForm.getPassword(), account.getPassword())) {
            errors.rejectValue("password", "WrongPassword");
        }
    }
}
