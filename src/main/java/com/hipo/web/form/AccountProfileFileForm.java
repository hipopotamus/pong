package com.hipo.web.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class AccountProfileFileForm {

    @NotNull
    private MultipartFile profileImgFile;
}
