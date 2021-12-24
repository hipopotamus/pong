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

        if (originalFilename.isBlank() || originalFilename.isEmpty()) {
            errors.reject("IllegalFileName", "파일 이름이 공백이거나 비어있습니다.");
        }

        if (!originalFilename.contains(".")) {
            errors.reject("IllegalFileName", "확장자가 없습니다.");
        }

        if (originalFilename.equals(".")) {
            errors.reject("IllegalFileName", "잘못된 형식의 파일이름입니다.");
        }

    }
}
