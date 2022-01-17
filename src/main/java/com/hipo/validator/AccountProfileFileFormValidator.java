package com.hipo.validator;

import com.hipo.dataobjcet.form.AccountProfileFileForm;
import com.hipo.domain.processor.FileProcessor;
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
        String originalFilename = accountProfileFileForm.getProfileFile().getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank() || originalFilename.isEmpty()) {
            errors.rejectValue("profileFile", "BlankFileName");
        } else if (!originalFilename.contains(".") || fileProcessor.extracted(originalFilename).equals(".")) {
            errors.rejectValue("profileFile", "NonExtractFileName");
        }
    }
}
