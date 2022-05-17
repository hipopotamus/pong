package com.hipo.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomMasterForm {

    @NotNull
    private Long chatRoomId;

    @NotNull
    private Long accountId;
}
