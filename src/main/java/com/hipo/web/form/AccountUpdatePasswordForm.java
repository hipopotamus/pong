package com.hipo.web.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class AccountUpdatePasswordForm {

    @NotBlank
    @Length(min = 4, max = 30)
    private String password;

    @NotBlank
    @Length(min = 4, max = 30)
    private String newPassword;

    @NotBlank
    @Length(min = 4, max = 30)
    private String confirmNewPassword;

}
