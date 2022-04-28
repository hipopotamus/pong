package com.hipo.security;

import com.google.gson.Gson;
import com.hipo.dataobjcet.dto.BasicErrorResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        BasicErrorResult authenticationException =
                new BasicErrorResult("401", "UnAuthorized", "인증되지 않았습니다.");
        String authenticationExJson = new Gson().toJson(authenticationException);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(authenticationExJson);

    }
}
