package com.hipo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import com.hipo.domain.processor.JwtProcessor;
import com.hipo.repository.AccountRepository;
import com.hipo.service.AccountService;
import com.hipo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.FileInputStream;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired AccountRepository accountRepository;
    @Autowired AccountService accountService;
    @Autowired AuthService authService;
    @Autowired JwtProcessor jwtProcessor;

    @BeforeEach
    public void init() {
        Account account = Account.builder()
                .username("test@test.com")
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname("testNickname")
                .profileImgName("/Users/hipo/Desktop/hipo_img/profile_img/default.jpeg")
                .role(Role.User)
                .gender(Gender.MAN)
                .birthDate(LocalDate.now())
                .build();
        accountRepository.save(account);
    }

    @Test
    @DisplayName("Account 생성 성공")
    public void createAccountTest() throws Exception {

        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "createTest@test.com");
        params.add("password", "1234");
        params.add("nickname", "createTestNickname");
        params.add("gender", "MAN");
        params.add("birthDate", "1890-01-01");

        MockMultipartFile file = new MockMultipartFile("profileImgFile", null, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo_img/profile_img/default.jpeg"));

        //when
        //** Account 생성
        mockMvc.perform(multipart("/account").file(file)
                        .params(params))
                .andExpect(status().isOk())
                .andReturn();

//        Account account = accountRepository.findByUsername("createTest@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));

        //then
//        assertThat(account.getUsername()).isEqualTo("createTest@test.com");
//        assertThat(bCryptPasswordEncoder.matches("1234", account.getPassword())).isTrue();
//        assertThat(account.getNickname()).isEqualTo("createTestNickname");
//        assertThat(account.getGender().toString()).isEqualTo("MAN");
//        assertThat(account.getBirthDate().toString()).isEqualTo("1890-01-01");
    }
//
//    @Test
//    @DisplayName("Account 생성 실패_email 형식이 아닌 username")
//    public void createAccount_illegalEmailUsername_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        //** email 형식이 아닌 username
//        params.add("username", "test");
//        params.add("password", "1234");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** email 형식이 아닌 username으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto emailError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** Email Error field, code 확인
//        assertThat(emailError.getField()).isEqualTo("username");
//        assertThat(emailError.getCode()).isEqualTo("Email");
//        assertThat(emailError.getMessage()).isEqualTo("이메일 형식이 아닙니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_빈 username")
//    public void createAccount_blankUsername_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        //** 빈 username
//        params.add("username", "");
//        params.add("password", "1234");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 빈 username으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto notBlankError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NotBlank Error field, code 확인
//        assertThat(notBlankError.getField()).isEqualTo("username");
//        assertThat(notBlankError.getCode()).isEqualTo("NotBlank");
//        assertThat(notBlankError.getMessage()).isEqualTo("아이디가 비어있습니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_중복된 username")
//    public void createAccount_duplicationUsername_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        //** 중복된 username
//        params.add("username", "test@test.com");
//        params.add("password", "1234");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 중복된 username으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto usernameDuplicationError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** usernameDuplication Error field, code 확인
//        assertThat(usernameDuplicationError.getField()).isEqualTo("username");
//        assertThat(usernameDuplicationError.getCode()).isEqualTo("UsernameDuplication");
//        assertThat(usernameDuplicationError.getMessage()).isEqualTo("이미 사용중인 아이디입니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_빈 password")
//    public void createAccount_blankPassword_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        //** 빈 password
//        params.add("password", "      ");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 빈 password로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto notBlankError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NotBlank Error field, code 확인
//        assertThat(notBlankError.getField()).isEqualTo("password");
//        assertThat(notBlankError.getCode()).isEqualTo("NotBlank");
//        assertThat(notBlankError.getMessage()).isEqualTo("비밀번호가 비어있습니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_잘못된 길이의 password")
//    public void createAccount_illegalLengthPassword_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        //** 잘못된 길이의 password(4자리 미만)
//        params.add("password", "1");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 잘못된 길이의 password(4자리 미만)로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto lengthError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** Length Error field, code 확인
//        assertThat(lengthError.getField()).isEqualTo("password");
//        assertThat(lengthError.getCode()).isEqualTo("Length");
//        assertThat(lengthError.getMessage()).isEqualTo("비밀번호는 4자리 이상 30자리 이하이여야 합니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_빈 nickname")
//    public void createAccount_blankNickname_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        //** 빈 nickname
//        params.add("nickname", "");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** //** 빈 nickname으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto notBlankError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NotBlank Error field, code 확인
//        assertThat(notBlankError.getField()).isEqualTo("nickname");
//        assertThat(notBlankError.getCode()).isEqualTo("NotBlank");
//        assertThat(notBlankError.getMessage()).isEqualTo("닉네임이 비어있습니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_잘못된 길이의 nickname")
//    public void createAccount_illegalLengthNickname_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        //** 잘못된 길이의 nickname(30자리 이상)
//        params.add("nickname", "01234567890123456789012345678901234567890123456789012345678901234567890123456789");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 잘못된 길이의 nickname(30자리 이상)으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto lengthError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** Length Error field, code 확인
//        assertThat(lengthError.getField()).isEqualTo("nickname");
//        assertThat(lengthError.getCode()).isEqualTo("Length");
//        assertThat(lengthError.getMessage()).isEqualTo("닉네임은 1자리 이상 30자리 이하이여야 합니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_잘못된 패턴의 nickname")
//    public void createAccount_illegalPatternNickname_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        //** 잘못된 패턴의 nickname
//        params.add("nickname", "!@ ");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 잘못된 패턴의 nickname으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto patternError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** Pattern Error field, code 확인
//        assertThat(patternError.getField()).isEqualTo("nickname");
//        assertThat(patternError.getCode()).isEqualTo("Pattern");
//        assertThat(patternError.getMessage()).isEqualTo("닉네임에 특수문자나 공백을 기입할 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_중복된 nickname")
//    public void createAccount_duplicationNickname_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        //** 중복된 nickname
//        params.add("nickname", "testNickname");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 중복된 nickname으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto nicknameDuplicationError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NicknameDuplication Error field, code 확인
//        assertThat(nicknameDuplicationError.getField()).isEqualTo("nickname");
//        assertThat(nicknameDuplicationError.getCode()).isEqualTo("NicknameDuplication");
//        assertThat(nicknameDuplicationError.getMessage()).isEqualTo("이미 사용중인 닉네임입니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_빈 profileFileName")
//    public void createAccount_blankFileName_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        params.add("nickname", "createNickname");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        //** 빈 profileFileName
//        MockMultipartFile file = new MockMultipartFile("profileFile", "  ", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 빈 profileFileName으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto BlankFileNameError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** BlankFileName Error field, code 확인
//        assertThat(BlankFileNameError.getField()).isEqualTo("profileFile");
//        assertThat(BlankFileNameError.getCode()).isEqualTo("BlankFileName");
//        assertThat(BlankFileNameError.getMessage()).isEqualTo("파일 이름이 공백이거나 비어있습니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_확장자가 없는 profileFileName")
//    public void createAccount_NonExtractFileName_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        params.add("birthDate", "1890-01-01");
//
//        //** 확장자가 없는 profileFileName
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 확장자가 없는 profileFileName으로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto BlankFileNameError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NonExtractFileName Error field, code 확인
//        assertThat(BlankFileNameError.getField()).isEqualTo("profileFile");
//        assertThat(BlankFileNameError.getCode()).isEqualTo("NonExtractFileName");
//        assertThat(BlankFileNameError.getMessage()).isEqualTo("확장자가 없습니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_null값의 gender")
//    public void createAccount_nullGender_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        params.add("nickname", "createTestNickname");
//        //** null값인 gender
//        params.add("gender", null);
//        params.add("birthDate", "1890-01-01");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** null값인 gender로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto notNullError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NotNull Error field, code 확인
//        assertThat(notNullError.getField()).isEqualTo("gender");
//        assertThat(notNullError.getCode()).isEqualTo("NotNull");
//        assertThat(notNullError.getMessage()).isEqualTo("null값이 들어갈 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_null값의 birthDate")
//    public void createAccount_nullBirthDate_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        //** null값인 birthDate
//        params.add("birthDate", null);
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** null값인 birthDate로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto notNullError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NotNull Error field, code 확인
//        assertThat(notNullError.getField()).isEqualTo("birthDate");
//        assertThat(notNullError.getCode()).isEqualTo("NotNull");
//        assertThat(notNullError.getMessage()).isEqualTo("null값이 들어갈 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_잘못된 형식의 birthDate")
//    public void createAccount_illegalPatternBirthDate_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        //** 잘못된 형식의 birthDate
//        params.add("birthDate", "1993-111-22");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 잘못된 형식의 birthDate로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto typeMismatchError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** typeMismatch Error field, code 확인
//        assertThat(typeMismatchError.getField()).isEqualTo("birthDate");
//        assertThat(typeMismatchError.getCode()).isEqualTo("typeMismatch");
//        assertThat(typeMismatchError.getMessage()).isEqualTo("생년월일은 'yyyy-MM-dd'과 같은 형식 이여야 합니다.");
//    }
//
//    @Test
//    @DisplayName("Account 생성 실패_현재보다 느린 birthDate")
//    public void createAccount_futureBirthDate_Test() throws Exception {
//
//        //given
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        params.add("username", "createTest@test.com");
//        params.add("password", "1234");
//        params.add("nickname", "createTestNickname");
//        params.add("gender", "MAN");
//        //** 현재보다 느린 birthDate
//        params.add("birthDate", "3993-12-22");
//
//        MockMultipartFile file = new MockMultipartFile("profileFile", "testFilename.jpeg", "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 현재보다 느린 birthDate로 Account 생성
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account").file(file)
//                        .params(params))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto futureBirthDateError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** FutureBirthDate Error field, code 확인
//        assertThat(futureBirthDateError.getField()).isEqualTo("birthDate");
//        assertThat(futureBirthDateError.getCode()).isEqualTo("FutureBirthDate");
//        assertThat(futureBirthDateError.getMessage()).isEqualTo("생년월일이 당일과 같거나 늦을 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("닉네임 수정 성공")
//    public void updateAccountNicknameTest() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        String nickname = "updateNickname";
//
//        AccountNicknameForm accountNicknameForm = new AccountNicknameForm(nickname);
//
//        //when
//        mockMvc.perform(post("/account/nickname")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken)
//                        .content(objectMapper.writeValueAsString(accountNicknameForm)))
//                .andExpect(status().isOk());
//
//        //then
//        assertThat(account.getNickname()).isEqualTo(nickname);
//    }
//
//    @Test
//    @DisplayName("닉네임 수정 실패_빈 nickname")
//    public void updateNickname_blankNickname_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** 빈 nickname
//        String nickname = "";
//
//        AccountNicknameForm accountNicknameForm = new AccountNicknameForm(nickname);
//
//        //when
//        //** 빈 nickname으로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(post("/account/nickname")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken)
//                        .content(objectMapper.writeValueAsString(accountNicknameForm)))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto notBlankError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NotBlank Error field, code 확인
//        assertThat(notBlankError.getField()).isEqualTo("nickname");
//        assertThat(notBlankError.getCode()).isEqualTo("NotBlank");
//        assertThat(notBlankError.getMessage()).isEqualTo("닉네임이 비어있습니다.");
//    }
//
//    @Test
//    @DisplayName("닉네임 수정 실패_잘못된 길이의 nickname")
//    public void updateNickname_illegalLengthNickname_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** 잘못된 길이의 nickname(30자리 이상)
//        String nickname = "01234567890123456789012345678901234567890123456789012345678901234567890123456789";
//
//        AccountNicknameForm accountNicknameForm = new AccountNicknameForm(nickname);
//
//        //when
//        //** 잘못된 길이의 nickname(30자리 이상)으로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(post("/account/nickname")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken)
//                        .content(objectMapper.writeValueAsString(accountNicknameForm)))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto lengthError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** Length Error field, code 확인
//        assertThat(lengthError.getField()).isEqualTo("nickname");
//        assertThat(lengthError.getCode()).isEqualTo("Length");
//        assertThat(lengthError.getMessage()).isEqualTo("닉네임은 1자리 이상 30자리 이하이여야 합니다.");
//    }
//
//    @Test
//    @DisplayName("닉네임 수정 실패_잘못된 패턴의 nickname")
//    public void updateNickname_illegalPatternNickname_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** 잘못된 패턴의 nickname
//        String nickname = "! #";
//
//        AccountNicknameForm accountNicknameForm = new AccountNicknameForm(nickname);
//
//        //when
//        //** 잘못된 패턴의 nickname으로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(post("/account/nickname")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken)
//                        .content(objectMapper.writeValueAsString(accountNicknameForm)))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto patternError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** Pattern Error field, code 확인
//        assertThat(patternError.getField()).isEqualTo("nickname");
//        assertThat(patternError.getCode()).isEqualTo("Pattern");
//        assertThat(patternError.getMessage()).isEqualTo("닉네임에 특수문자나 공백을 기입할 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("닉네임 수정 실패_중복된 nickname")
//    public void updateNickname_duplicationNickname_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** 중복된 nickname
//        String nickname = "testNickname";
//
//        AccountNicknameForm accountNicknameForm = new AccountNicknameForm(nickname);
//
//        //when
//        //** 중복된 nickname으로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(post("/account/nickname")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", jwtToken)
//                        .content(objectMapper.writeValueAsString(accountNicknameForm)))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto nicknameDuplicationError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NicknameDuplication Error field, code 확인
//        assertThat(nicknameDuplicationError.getField()).isEqualTo("nickname");
//        assertThat(nicknameDuplicationError.getCode()).isEqualTo("NicknameDuplication");
//        assertThat(nicknameDuplicationError.getMessage()).isEqualTo("이미 사용중인 닉네임입니다.");
//    }
//
//    @Test
//    @DisplayName("프로필 이미지 수정 성공")
//    public void updateProfileImgTest() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        String fileName = "updateProfileImg.jpeg";
//        MockMultipartFile file = new MockMultipartFile("profileFile", fileName, "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        String beforeProfileImgPath = account.getProfileImgName();
//
//        //when
//        mockMvc.perform(multipart("/account/profileImg").file(file)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().isOk());
//
//        //then
//        assertThat(account.getProfileImgName()).isNotEqualTo(beforeProfileImgPath);
//    }
//
//    @Test
//    @DisplayName("프로필 이미지 수정 실패_빈 profileFileName")
//    public void updateProfileImg_blankFileName_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** 빈 profileFileName
//        String fileName = "  ";
//        MockMultipartFile file = new MockMultipartFile("profileFile", fileName, "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 빈 profileFileName으로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account/profileImg").file(file)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto BlankFileNameError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** BlankFileName Error field, code 확인
//        assertThat(BlankFileNameError.getField()).isEqualTo("profileFile");
//        assertThat(BlankFileNameError.getCode()).isEqualTo("BlankFileName");
//        assertThat(BlankFileNameError.getMessage()).isEqualTo("파일 이름이 공백이거나 비어있습니다.");
//    }
//
//    @Test
//    @DisplayName("프로필 이미지 수정 실패_확장자가 없는 profileFileName")
//    public void updateProfileImg_NonExtractFileName_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** 확장자가 없는 profileFileName
//        String fileName = "test";
//        MockMultipartFile file = new MockMultipartFile("profileFile", fileName, "image/jpeg",
//                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
//
//        //when
//        //** 확장자가 없는 profileFileName으로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(multipart("/account/profileImg").file(file)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto BlankFileNameError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NonExtractFileName Error field, code 확인
//        assertThat(BlankFileNameError.getField()).isEqualTo("profileFile");
//        assertThat(BlankFileNameError.getCode()).isEqualTo("NonExtractFileName");
//        assertThat(BlankFileNameError.getMessage()).isEqualTo("확장자가 없습니다.");
//    }
//
//    @Test
//    @DisplayName("생년월일 수정 성공")
//    public void updateBrithDateTest() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        LocalDate birthDate = LocalDate.of(1000, 10, 10);
//
//        AccountBirthDateForm accountBirthDateForm = new AccountBirthDateForm(birthDate);
//
//        //when
//        mockMvc.perform(post("/account/birthDate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(accountBirthDateForm))
//                        .header("Authorization", jwtToken))
//                .andExpect(status().isOk());
//
//        //then
//        assertThat(account.getBirthDate()).isEqualTo(birthDate);
//    }
//
//    @Test
//    @DisplayName("생년월일 수정 실패_null값의 birthDate")
//    public void updateBirthDate_nullBirthDate_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** null값인 birthDate
//        LocalDate birthDate = null;
//
//        AccountBirthDateForm accountBirthDateForm = new AccountBirthDateForm(birthDate);
//
//        //when
//        //** null값인 birthDate로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(post("/account/birthDate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(accountBirthDateForm))
//                        .header("Authorization", jwtToken))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto notNullError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NotNull Error field, code 확인
//        assertThat(notNullError.getField()).isEqualTo("birthDate");
//        assertThat(notNullError.getCode()).isEqualTo("NotNull");
//        assertThat(notNullError.getMessage()).isEqualTo("null값이 들어갈 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("생년월일 수정 실패_잘못된 형식의 birthDate")
//    public void updateBirthDate_illegalPatternBirthDate_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** 잘못된 형식의 birthDate
//        String httpMessageNotReadableBirthDate = "{\"birthDate\":\"1000-1222-01\"}";
//
//        //when
//        //** 잘못된 형식의 birthDate로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(post("/account/birthDate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(httpMessageNotReadableBirthDate)
//                        .header("Authorization", jwtToken))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        BasicErrorResult basicErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                BasicErrorResult.class);
//
//        //then
//        assertThat(basicErrorResult.getState()).isEqualTo("400");
//        assertThat(basicErrorResult.getException()).isEqualTo(HttpMessageNotReadableException.class.getSimpleName());
//    }
//
//    @Test
//    @DisplayName("생년월일 수정 실패_현재보다 느린 birthDate")
//    public void updateBirthDate_futureBirthDate_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** 현재보다 느린 birthDate
//        LocalDate birthDate = LocalDate.of(4000, 10, 10);
//
//        AccountBirthDateForm accountBirthDateForm = new AccountBirthDateForm(birthDate);
//
//        //when
//        //** 현재보다 느린 birthDate로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(post("/account/birthDate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(accountBirthDateForm))
//                        .header("Authorization", jwtToken))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto futureBirthDateError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** FutureBirthDate Error field, code 확인
//        assertThat(futureBirthDateError.getField()).isEqualTo("birthDate");
//        assertThat(futureBirthDateError.getCode()).isEqualTo("FutureBirthDate");
//        assertThat(futureBirthDateError.getMessage()).isEqualTo("생년월일이 당일과 같거나 늦을 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("성별 수정 성공")
//    public void updateGenderTest() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        Gender gender = Gender.FEMALE;
//
//        AccountGenderForm accountGenderForm = new AccountGenderForm(gender);
//
//        //when
//        mockMvc.perform(post("/account/gender")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(accountGenderForm))
//                        .header("Authorization", jwtToken))
//                .andExpect(status().isOk());
//
//        //then
//        assertThat(account.getGender()).isEqualTo(gender);
//    }
//
//    @Test
//    @DisplayName("성별 수정 실패_null값의 gender")
//    public void updateGender_nullGender_Test() throws Exception {
//
//        //given
//        Account account = accountRepository.findByUsername("test@test.com")
//                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));
//        String jwtToken = "Bearer " + jwtProcessor.createAuthJwtToken(new UserAccount(account));
//
//        //** null값인 gender
//        Gender gender = null;
//
//        AccountGenderForm accountGenderForm = new AccountGenderForm(gender);
//
//        //when
//        //** null값인 gender로 Account 수정
//        MvcResult illegalFormResult = mockMvc.perform(post("/account/gender")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(accountGenderForm))
//                        .header("Authorization", jwtToken))
//                .andExpect(status().is4xxClientError())
//                .andReturn();
//
//        FormErrorResult formErrorResult = objectMapper.readValue(illegalFormResult.getResponse().getContentAsString(),
//                FormErrorResult.class);
//        ErrorDto notNullError = formErrorResult.getErrorList().get(0);
//
//        //then
//        assertThat(formErrorResult.getState()).isEqualTo("400");
//        assertThat(formErrorResult.getException()).isEqualTo(IllegalFormException.class.getSimpleName());
//
//        //** NotNull Error field, code 확인
//        assertThat(notNullError.getField()).isEqualTo("gender");
//        assertThat(notNullError.getCode()).isEqualTo("NotNull");
//        assertThat(notNullError.getMessage()).isEqualTo("null값이 들어갈 수 없습니다.");
//    }
}