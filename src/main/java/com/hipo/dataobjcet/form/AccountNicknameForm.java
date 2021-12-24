package com.hipo.dataobjcet.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class AccountNicknameForm {

    @NotEmpty
    @Length(max = 30)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{1,30}$")
    private String nickname;
}
