package com.hipo.dataobjcet.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class AccountProfileFileForm {

    @NotNull
    private MultipartFile profileFile;
}
