package com.hipo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hipo.dataobjcet.dto.BasicErrorResult;
import com.hipo.dataobjcet.dto.ErrorDto;
import com.hipo.dataobjcet.dto.FormErrorResult;
import com.hipo.dataobjcet.form.LoginForm;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import com.hipo.domain.processor.JwtProcessor;
import com.hipo.exception.IllegalFormException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.properties.JwtProperties;
import com.hipo.repository.AccountRepository;
import com.hipo.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountService accountService;
    @Autowired JwtProcessor jwtProcessor;
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;


    @BeforeEach
    public void init() {
        Account account = new Account("test@test.com", bCryptPasswordEncoder.encode("1234"),
                "testNickname", "/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg",
                Role.User, Gender.MAN, LocalDate.now());
        accountRepository.save(account);
    }


    @Test
    @DisplayName("로그인 성공")
    public void loginTest() throws Exception {

        //given
        LoginForm loginForm = new LoginForm("test@test.com", "1234");

        //when
        MvcResult mvcResult = mockMvc.perform(post("/myLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andReturn();

        //** 발급된 jwtToken 확인
        String jwtHeader = mvcResult.getResponse().getHeader("Authorization");
        String jwtToken = jwtProcessor.extractBearer(jwtHeader);
        String decodeUsername = jwtProcessor.decodeJwtToken(jwtToken, JwtProperties.SECRET, "username");

        //then
        assertThat(decodeUsername).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("로그인 실패_잘못된 패스워드")
    public void login_wrongPassword_Test() throws Exception {

        //given
        //** 잘못된 패스워드
        LoginForm wrongPasswordLoginForm = new LoginForm("test@test.com", "12345");

        //when
        MvcResult illegalFromResult = mockMvc.perform(post("/myLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongPasswordLoginForm)))
                .andExpect(status().is4xxClientError())
                .andReturn();

        FormErrorResult formErrorResult = objectMapper.readValue(
                illegalFromResult.getResponse().getContentAsString(), FormErrorResult.class);
        ErrorDto wrongPasswordError = formErrorResult.getErrorList().get(0);

        //then
        assertThat(formErrorResult.getState()).isEqualTo("400");
        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());

        //** WrongPassword error code, field 확인
        assertThat(wrongPasswordError.getField()).isEqualTo("password");
        assertThat(wrongPasswordError.getCode()).isEqualTo("WrongPassword");
        assertThat(wrongPasswordError.getMessage()).isEqualTo("비밀번호가 맞지 않습니다.");

    }

    @Test
    @DisplayName("로그인 실패_잘못된 아이디")
    public void login_wrongUsername_Test() throws Exception {

        //given
        //** 잘못된 아이디
        LoginForm nonExistResourceLoginForm = new LoginForm("wrongTest@test.com", "1234");

        //when
        MvcResult nonExistResourceResult = mockMvc.perform(post("/myLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistResourceLoginForm)))
                .andExpect(status().is4xxClientError())
                .andReturn();

        BasicErrorResult nonExistResourceError = objectMapper.readValue(
                nonExistResourceResult.getResponse().getContentAsString(), BasicErrorResult.class);

        //then
        assertThat(nonExistResourceError.getState()).isEqualTo("400");
        assertThat(nonExistResourceError.getException()).isEqualTo(NonExistResourceException.class.getSimpleName());
    }
}