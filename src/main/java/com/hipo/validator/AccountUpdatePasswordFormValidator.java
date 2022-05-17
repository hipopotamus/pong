package com.hipo.validator;

import com.hipo.domain.entity.Account;
import com.hipo.security.UserAccount;
import com.hipo.web.form.AccountUpdatePasswordForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountUpdatePasswordFormValidator implements Validator {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountUpdatePasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountUpdatePasswordForm accountUpdatePasswordForm = (AccountUpdatePasswordForm) target;

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = userAccount.getAccount();

        //현재 비밀번호 확인//
        if (!bCryptPasswordEncoder.matches(accountUpdatePasswordForm.getPassword(), account.getPassword())) {
            errors.rejectValue("password", "WrongPassword");
        }
        //현재 비밀번호 확인//

        //현재 비밀번호와 새 비밀번호 일치 확인//
        if (bCryptPasswordEncoder.matches(accountUpdatePasswordForm.getNewPassword(), account.getPassword())) {
            errors.reject("SamePassword");
        }
        //현재 비밀번호와 새 비밀번호 일치 확인//

        //새 비밀번호 확인//
        if (!accountUpdatePasswordForm.getNewPassword().equals(accountUpdatePasswordForm.getConfirmNewPassword())) {
            errors.reject("Discord");
        }
        //새 비밀번호 확인//
    }
}
