package com.hipo.validator;

import com.hipo.web.form.AccountProfileFileForm;
import com.hipo.utill.FileProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountProfileFileFormValidator implements Validator {

    private final FileProcessor fileProcessor;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountProfileFileForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AccountProfileFileForm accountProfileFileForm = (AccountProfileFileForm) target;
        String originalFilename = accountProfileFileForm.getProfileImgFile().getOriginalFilename();
        String extracted = fileProcessor.extracted(originalFilename);

        if (originalFilename.isBlank()) {
            errors.rejectValue("profileImgFile", "BlankFileName");
        } else if (!originalFilename.contains(".") || extracted.isEmpty()) {
            errors.rejectValue("profileImgFile", "NonExtractFileName");
        }
    }
}
