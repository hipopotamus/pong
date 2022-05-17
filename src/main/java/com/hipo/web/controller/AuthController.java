package com.hipo.web.controller;

import com.hipo.properties.JwtProperties;
import com.hipo.service.AuthService;
import com.hipo.web.dto.ResultMessage;
import com.hipo.web.form.LoginForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/myLogin")
    public ResultMessage myLogin(@Valid @RequestBody LoginForm loginForm, HttpServletResponse response) {
        String jwtToken = authService.login(loginForm.getUsername(), loginForm.getPassword());
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + " " + jwtToken);

        return new ResultMessage("success login");
    }
}
