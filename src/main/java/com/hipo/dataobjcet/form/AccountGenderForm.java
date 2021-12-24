package com.hipo.dataobjcet.form;

import com.hipo.domain.entity.enums.Gender;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountGenderForm {

    @NotNull
    private Gender gender;
}
