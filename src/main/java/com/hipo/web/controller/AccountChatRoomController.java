package com.hipo.web.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.service.AccountChatRoomService;
import com.hipo.web.dto.ResultMessage;
import com.hipo.web.form.AccountChatRoomForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountChatRoomController {

    private final AccountChatRoomService accountChatRoomService;

    @PostMapping("/accountChatRoom")
    public ResultMessage createAccountChatRoom(@LoginAccountId Long loginAccountId,
                                               @Valid @RequestBody AccountChatRoomForm accountChatRoomForm) {

        accountChatRoomService.createAccountChatRoom(loginAccountId, accountChatRoomForm.getAccountId(),
                accountChatRoomForm.getChatRoomId());

        return new ResultMessage("success create accountChatRoom");
    }
}
