package com.hipo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hipo.dataobjcet.dto.BasicErrorResult;
import com.hipo.dataobjcet.dto.ErrorDto;
import com.hipo.dataobjcet.dto.FormErrorResult;
import com.hipo.dataobjcet.form.LoginForm;
import com.hipo.domain.UserAccount;
import com.hipo.domain.processor.JwtProcessor;
import com.hipo.exception.IllegalFormException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.properties.JwtProperties;
import com.hipo.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JwtProcessor jwtProcessor;

    @Test
    @DisplayName("로그인 성공")
    public void loginTest() throws Exception {

        //given
        String loginUsername = "test1@naver.com";
        String password = "1234";

        LoginForm loginForm = new LoginForm(loginUsername, password);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/myLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andReturn();

        //** 발급된 jwtToken 확인
        String jwtHeader = mvcResult.getResponse().getHeader("Authorization");
        String jwtToken = jwtProcessor.extractBearer(jwtHeader);
        String username = jwtProcessor.decodeJwtToken(jwtToken, JwtProperties.SECRET, "username");

        //** securityContextHolder 확인
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String securityContextUsername = userAccount.getAccount().getUsername();

        //then
        assertThat(username).isEqualTo(loginUsername);
        assertThat(username).isEqualTo(securityContextUsername);
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 패스워드")
    public void login_wrongPassword_Test() throws Exception {

        //given
        String loginUsername = "test1@naver.com";
        String password = "12345"; //** 잘못된 패스워드

        LoginForm loginForm = new LoginForm(loginUsername, password);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/myLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andReturn();

        String formErrorResultByJson = mvcResult.getResponse().getContentAsString();

        FormErrorResult formErrorResult = objectMapper.readValue(formErrorResultByJson, FormErrorResult.class);

        ErrorDto wrongPasswordErrorDto = formErrorResult.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("WrongPassword"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("WrongPassword를 갖는 error가 없습니다."));

        //then
        assertThat(formErrorResult.getState()).isEqualTo("400");
        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());

        assertThat(wrongPasswordErrorDto.getField()).isEqualTo("password");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 아이디")
    public void login_wrongUsername_Test() throws Exception {

        //given
        String loginUsername = "wrongTest1@naver.com"; //** 잘못된 아이디
        String password = "1234";

        LoginForm loginForm = new LoginForm(loginUsername, password);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/myLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andReturn();

        String formErrorResultByJson = mvcResult.getResponse().getContentAsString();

        BasicErrorResult basicErrorResult = objectMapper.readValue(formErrorResultByJson, BasicErrorResult.class);

        //then
        assertThat(basicErrorResult.getState()).isEqualTo("400");
        assertThat(basicErrorResult.getException()).isEqualTo(NonExistResourceException.class.getSimpleName());
    }

}