package com.hipo.dataobjcet.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountChatRoomForm {

    private Long accountId;

    private Long chatRoomId;

    public AccountChatRoomForm(Long accountId, Long chatRoomId) {
        this.accountId = accountId;
        this.chatRoomId = chatRoomId;
    }
}
