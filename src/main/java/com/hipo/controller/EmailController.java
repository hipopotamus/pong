package com.hipo.controller;

import com.hipo.argumentresolver.LoginAccountId;
import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.dataobjcet.form.EmailTokenForm;
import com.hipo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResultMessage sendConfirmEmail(@ApiIgnore @LoginAccountId Long accountId) {
        emailService.sendSignUpConfirmEmail(accountId);
        return new ResultMessage("success send email");
    }

    @PostMapping("/email/confirm")
    public ResultMessage confirmEmail(@ApiIgnore @LoginAccountId Long accountId, @RequestBody EmailTokenForm emailTokenForm) {
        emailService.confirmEmail(accountId, emailTokenForm.getEmailToken());
        return new ResultMessage("success confirm Email");
    }
}
