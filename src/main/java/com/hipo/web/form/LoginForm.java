package com.hipo.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {

    @Email
    @NotEmpty
    private String username;

    @NotEmpty
    @Length(min = 4, max = 30)
    private String password;
}
