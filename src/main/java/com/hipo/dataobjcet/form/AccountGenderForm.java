package com.hipo.dataobjcet.form;

import com.hipo.domain.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountGenderForm {

    @NotNull
    private Gender gender;
}
