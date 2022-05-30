package com.hipo.web.controllerAdvice;

import com.hipo.validator.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@Component
@RequiredArgsConstructor
public class InitBinderControllerAdvice {

    private final AccountFormValidator accountFormValidator;
    private final AccountUpdateInfoFormValidator accountUpdateInfoFormValidator;
    private final AccountProfileFileFormValidator accountProfileFileFormValidator;
    private final AccountUpdatePasswordFormValidator accountUpdatePasswordFormValidator;
    private final LoginFormValidator loginFormValidator;


    //AccountController//
    @InitBinder("accountForm")
    public void accountFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountFormValidator);
    }

    @InitBinder("accountUpdateInfoForm")
    public void accountUpdateInfoFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountUpdateInfoFormValidator);
    }

    @InitBinder("accountProfileFileForm")
    public void accountProfileFileFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountProfileFileFormValidator);
    }

    @InitBinder("accountUpdatePasswordForm")
    public void accountUpdatePasswordFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(accountUpdatePasswordFormValidator);
    }
    //AccountController//

    //AuthController//
    @InitBinder("loginForm")
    public void loginFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(loginFormValidator);
    }
    //AuthController//
}
