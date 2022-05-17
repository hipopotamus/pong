package com.hipo.validator;

import com.hipo.web.form.AccountForm;
import com.hipo.utill.FileProcessor;
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
    private final FileProcessor fileProcessor;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountForm accountForm = (AccountForm) target;

        //** username
        if (accountRepository.existsByUsername(accountForm.getUsername())) {
            errors.rejectValue("username", "UsernameDuplication");
        }

        //** nickname
        if (accountRepository.existsByNickname(accountForm.getNickname())) {
            errors.rejectValue("nickname", "NicknameDuplication");
        }

        //** profileFile
        if (accountForm.getProfileImgFile() != null) {
            String originalFilename = accountForm.getProfileImgFile().getOriginalFilename();
            String extracted = fileProcessor.extracted(originalFilename);

            if (originalFilename.isBlank()) {
                errors.rejectValue("profileImgFile", "BlankFileName");
            } else if (!originalFilename.contains(".") || extracted.isEmpty()) {
                errors.rejectValue("profileImgFile", "NonExtractFileName");
            }
        }

        //** birthDate
        LocalDate birthDate = accountForm.getBirthDate();
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            errors.rejectValue("birthDate", "FutureBirthDate");
        }
    }
}
