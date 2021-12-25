package com.hipo.controller;

import com.hipo.dataobjcet.dto.MessageResult;
import com.hipo.dataobjcet.form.LoginForm;
import com.hipo.exception.IllegalFormException;
import com.hipo.properties.JwtProperties;
import com.hipo.service.AuthService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = {"1. Auth"})
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/myLogin")
    public MessageResult myLogin(@Valid @RequestBody LoginForm loginForm, @ApiIgnore Errors errors,
                                 HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new IllegalFormException(errors);
        }

        String jwtToken = authService.login(loginForm.getUsername(), loginForm.getPassword());
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + " " + jwtToken);

        return new MessageResult("success login");
    }
}
