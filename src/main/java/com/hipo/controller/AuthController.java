package com.hipo.controller;

import com.hipo.dataobjcet.dto.ResultMessage;
import com.hipo.dataobjcet.form.LoginForm;
import com.hipo.exception.IllegalFormException;
import com.hipo.properties.JwtProperties;
import com.hipo.service.AuthService;
import com.hipo.validator.LoginFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final LoginFormValidator loginFormValidator;

    @InitBinder("loginForm")
    public void loginFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(loginFormValidator);
    }

    @PostMapping("/myLogin")
    public ResultMessage myLogin(@Valid @RequestBody LoginForm loginForm, Errors errors,
                                 HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        String jwtToken = authService.login(loginForm.getUsername(), loginForm.getPassword());
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + " " + jwtToken);

        return new ResultMessage("success login");
    }
}
