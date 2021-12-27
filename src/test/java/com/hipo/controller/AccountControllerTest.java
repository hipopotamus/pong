package com.hipo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hipo.dataobjcet.dto.ErrorDto;
import com.hipo.dataobjcet.dto.FormErrorResult;
import com.hipo.domain.entity.Account;
import com.hipo.exception.IllegalFormException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("Account 생성 성공")
    public void createAccountTest() throws Exception {

        //given

        String username = "test@naver.com";
        String password = "1234";
        String nickname = "testNickname";
        String gender = "MAN";
        String birthDate = "1890-01-01";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", password);
        params.add("nickname", nickname);
        params.add("gender", gender);
        params.add("birthDate", birthDate);

        String fileName = "hipo.jpeg";
        MockMultipartFile file = new MockMultipartFile("profileFile", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/main/resources/static/sample/ea451747-9643" +
                        "-470f-9605-81407fc077c7.jpeg"));

        //when
        mockMvc.perform(multipart("/account").file(file)
                .params(params))
                .andReturn();

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));

        //then
        assertThat(account.getUsername()).isEqualTo(username);
        assertThat(bCryptPasswordEncoder.matches(password, account.getPassword())).isTrue();
        assertThat(account.getNickname()).isEqualTo(nickname);
        assertThat(account.getGender().toString()).isEqualTo(gender);
        assertThat(account.getBirthDate().toString()).isEqualTo(birthDate);
    }

    @Test
    @DisplayName("Account 생성 실패_잘못된 formData")
    public void createAccount_WrongForm_Test() throws Exception {

        //given
        String username = "test1@naver.com"; //** 중복
        String password = "123"; //** 잘못된 password
        String nickname = "testNickname1"; //** 중복
        String gender = "MAN";
        String birthDate = "3290-01-01"; //** 잘못된 birthDate;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", password);
        params.add("nickname", nickname);
        params.add("gender", gender);
        params.add("birthDate", birthDate);

        String fileName = "  "; //** 잘못된 fileName;
        MockMultipartFile file = new MockMultipartFile("profileFile", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/main/resources/static/sample/ea451747-9643" +
                        "-470f-9605-81407fc077c7.jpeg"));

        //when
        MvcResult mvcResult = mockMvc.perform(multipart("/account").file(file)
                        .params(params))
                .andReturn();

        String formErrorResultByJson = mvcResult.getResponse().getContentAsString();
        FormErrorResult formErrorResult = objectMapper.readValue(formErrorResultByJson, FormErrorResult.class);

        //then
        assertThat(formErrorResult.getState()).isEqualTo("400");
        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());

        //** usernameDuplication Error code, field 확인
        ErrorDto usernameDuplicationErrorDto = formErrorResult.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("usernameDuplication"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("usernameDuplication을 갖는 error가 없습니다."));
        assertThat(usernameDuplicationErrorDto.getField()).isEqualTo("username");

        //** nicknameDuplication Error code, field 확인
        ErrorDto nicknameDuplicationErrorDto = formErrorResult.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("nicknameDuplication"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("nicknameDuplication을 갖는 error가 없습니다."));
        assertThat(nicknameDuplicationErrorDto.getField()).isEqualTo("nickname");

        //** password Length Error code, field 확인
        ErrorDto lengthErrorDto = formErrorResult.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("Length"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Length을 갖는 error가 없습니다."));
        assertThat(lengthErrorDto.getField()).isEqualTo("password");

        //** IllegalFileName Error code, field 확인
        ErrorDto illegalFileNameErrorDto = formErrorResult.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("IllegalFileName"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("IllegalFileName을 갖는 error가 없습니다."));
        assertThat(illegalFileNameErrorDto.getField()).isEqualTo("profileFile");

        //** IllegalBirthDate Error code, field 확인
        ErrorDto illegalBirthDateErrorDto = formErrorResult.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("IllegalBirthDate"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("IllegalBirthDate을 갖는 error가 없습니다."));
        assertThat(illegalBirthDateErrorDto.getField()).isEqualTo("birthDate");
    }

    @Test
    @WithUserDetails("test1@naver.com")
    public void updateAccountNicknameTest() {

    }

}