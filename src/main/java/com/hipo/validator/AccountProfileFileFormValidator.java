package com.hipo.validator;

import com.hipo.dataobjcet.form.AccountProfileFileForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AccountProfileFileFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountProfileFileForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AccountProfileFileForm accountProfileFileForm = (AccountProfileFileForm) target;
        String originalFilename = accountProfileFileForm.getProfileFile().getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank() || originalFilename.isEmpty()) {
            errors.rejectValue("profileFile", "BlankFileName", "파일 이름이 공백이거나 비어있습니다.");
        } else if (!originalFilename.contains(".")) {
            errors.rejectValue("profileFile", "NonExtractFileName", "확장자가 없습니다.");
        } else if (originalFilename.equals(".")) {
            errors.rejectValue("profileFile", "OnlyDotFileName", "잘못된 형식의 파일이름입니다.");
        }

    }
}
