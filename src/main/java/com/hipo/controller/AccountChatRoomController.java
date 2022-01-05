package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.dataobjcet.form.AccountChatRoomForm;
import com.hipo.exception.IllegalFormException;
import com.hipo.service.AccountChatRoomService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = {"5. AccountChatRoom"})
@RestController
@RequiredArgsConstructor
public class AccountChatRoomController {

    private final AccountChatRoomService accountChatRoomService;

    @PostMapping("/accountChatRoom")
    public ResultMessage createAccountChatRoom(@ApiIgnore @LoginAccountId Long loginAccountId,
                                               @Valid @RequestBody AccountChatRoomForm accountChatRoomForm,
                                               @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        accountChatRoomService.createAccountChatRoom(loginAccountId, accountChatRoomForm.getAccountId(),
                accountChatRoomForm.getChatRoomId());

        return new ResultMessage("success create accountChatRoom");
    }
}
