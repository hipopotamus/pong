package com.hipo.validator;

import com.hipo.dataobjcet.form.AccountForm;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AccountFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountForm accountForm = (AccountForm) target;

        //** username
        if (accountRepository.existsByUsername(accountForm.getUsername())) {
            errors.rejectValue("username", "usernameDuplication", "이미 사용중인 아이디입니다.");
        }

        //** nickname
        if (accountRepository.existsByNickname(accountForm.getNickname())) {
            errors.rejectValue("nickname", "nicknameDuplication", "이미 사용중인 닉네임입니다.");
        }

        //** profileFile
        String originalFilename = accountForm.getProfileFile().getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank() || originalFilename.isEmpty()) {
            errors.rejectValue("profileFile", "BlankFileName", "파일 이름이 공백이거나 비어있습니다.");
        } else if (!originalFilename.contains(".")) {
            errors.rejectValue("profileFile", "NonExtractFileName", "확장자가 없습니다.");
        } else if (originalFilename.equals(".")) {
            errors.rejectValue("profileFile", "OnlyDotFileName", "잘못된 형식의 파일이름입니다.");
        }

        //** birthDate
        LocalDate birthDate = accountForm.getBirthDate();
        if (birthDate != null && (birthDate.isAfter(LocalDate.now()) || birthDate.isEqual(LocalDate.now()))) {
            errors.rejectValue("birthDate", "FutureBirthDate", "생년월일이 당일과 같거나 빠를 수 없습니다.");
        }
    }
}
