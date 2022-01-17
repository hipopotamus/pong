package com.hipo.dataobjcet.form;

import com.hipo.domain.entity.enums.Gender;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class AccountForm {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    @Length(min = 4, max = 30)
    private String password;

    @NotBlank
    @Length(max = 30)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{0,9999}$")
    private String nickname;

    @NotNull
    private MultipartFile profileFile;

    @NotNull
    private Gender gender;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
