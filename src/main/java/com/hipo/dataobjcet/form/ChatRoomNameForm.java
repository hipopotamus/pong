package com.hipo.dataobjcet.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ChatRoomNameForm {

    @NotBlank
    @Length(min = 1, max = 30)
    private String name;
}
