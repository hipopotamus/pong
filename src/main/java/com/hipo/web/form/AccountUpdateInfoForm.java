package com.hipo.web.form;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class AccountUpdateInfoForm {

    @Length(max = 30)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{0,9999}$")
    private String nickname;

    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    public Account toAccount() {
        return Account.builder()
                .nickname(this.nickname)
                .gender(this.gender)
                .birthDate(this.birthDate)
                .build();
    }
}
