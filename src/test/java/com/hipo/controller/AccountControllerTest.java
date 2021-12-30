package com.hipo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hipo.dataobjcet.dto.BasicErrorResult;
import com.hipo.dataobjcet.dto.ErrorDto;
import com.hipo.dataobjcet.dto.FormErrorResult;
import com.hipo.dataobjcet.form.AccountBirthDateForm;
import com.hipo.dataobjcet.form.AccountGenderForm;
import com.hipo.dataobjcet.form.AccountNicknameForm;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.exception.IllegalFormException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.service.AccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @Autowired
    AccountService accountService;

    static String originUsername = "test";
    static String originEmail = "@test.com";
    static String originPassword = "1234";
    static String originNickname = "testNickname";
    static Gender originGender = Gender.MAN;
    static LocalDate originBirthDate = LocalDate.now();
    static String originFileName = "test.jpeg";
    static MockMultipartFile originFile;

    static {
        try {
            originFile = new MockMultipartFile("profileFile", originFileName, "image/jpeg",
                    new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    void init() throws Exception {
        for (int i = 1; i <= 10; i++) {
            accountService.createAccount(originUsername + i + originEmail, originPassword, originNickname + i,
                    originFile, originGender, originBirthDate);
        }
    }

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
                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));

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
        String username = originUsername + 1 + originEmail; //** 중복된 username
        String password = "123"; //** 최소 길이보다 짧은 password
        String nickname = originNickname + 1; //** 중복된 nickname
        String gender = "MAN";
        String birthDate = "1000-021-01"; //** 잘못된 패턴인 birthDate;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("password", password);
        params.add("nickname", nickname);
        params.add("gender", gender);
        params.add("birthDate", birthDate);

        String fileName = "  "; //** 비어있는 fileName;
        MockMultipartFile file = new MockMultipartFile("profileFile", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));

        //when
        MvcResult IllegalFormResult = mockMvc.perform(multipart("/account").file(file)
                        .params(params))
                .andReturn();

        FormErrorResult formErrorResult = objectMapper.readValue(
                IllegalFormResult.getResponse().getContentAsString(), FormErrorResult.class);

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

        //** BlankFileName Error code, field 확인
        ErrorDto blankFileNameErrorDto = formErrorResult.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("BlankFileName"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("BlankFileName을 갖는 error가 없습니다."));
        assertThat(blankFileNameErrorDto.getField()).isEqualTo("profileFile");

        //** IllegalBirthDate Error code, field 확인
        ErrorDto typeMismatchErrorDto = formErrorResult.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("typeMismatch"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("typeMismatch를 갖는 error가 없습니다."));
        assertThat(typeMismatchErrorDto.getField()).isEqualTo("birthDate");
    }

    @Test
    @DisplayName("닉네임 수정 성공")
    @WithUserDetails("test1@test.com")
    public void updateAccountNicknameTest() throws Exception {

        //given
        String nickname = "updateNickname";

        AccountNicknameForm accountNicknameForm = new AccountNicknameForm(nickname);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/account/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountNicknameForm)))
                .andExpect(status().isOk())
                .andReturn();

        Account account = accountRepository.findByUsername(originUsername + 1 + originEmail)
                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));

        //then
        assertThat(account.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("닉네임 수정 실패_잘못된 formData")
    @WithUserDetails("test1@test.com")
    public void updateAccountNickname_IllegalFormData_Test() throws Exception {

        //given
        String lengthNickname = "updateNicknameupdateNicknameupdateNicknameupdateNicknameupdateNickname"; //** 최대 길이(30자 이상)보다 긴 nickname
        String patternNickname = ".!?"; //** 잘못된 형식의 nickname
        String nicknameDuplicationNickname = originNickname + 1; //** 중복된 nickname

        AccountNicknameForm lengthNicknameForm = new AccountNicknameForm(lengthNickname);
        AccountNicknameForm patternNicknameForm = new AccountNicknameForm(patternNickname);
        AccountNicknameForm nicknameDuplicationNicknameForm = new AccountNicknameForm(nicknameDuplicationNickname);

        //when
        MvcResult lengthResult = mockMvc.perform(post("/account/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lengthNicknameForm)))
                .andReturn();
        MvcResult patternResult = mockMvc.perform(post("/account/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patternNicknameForm)))
                .andReturn();
        MvcResult nicknameDuplicationResult = mockMvc.perform(post("/account/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nicknameDuplicationNicknameForm)))
                .andReturn();

        FormErrorResult lengthError = objectMapper.readValue(lengthResult.getResponse().getContentAsString(),
                FormErrorResult.class);
        FormErrorResult patternError = objectMapper.readValue(patternResult.getResponse().getContentAsString(),
                FormErrorResult.class);
        FormErrorResult nicknameDuplicationError = objectMapper.readValue(nicknameDuplicationResult.getResponse().getContentAsString(),
                FormErrorResult.class);

        //then
        //** Length Error code, field 확인
        ErrorDto lengthErrorDto = lengthError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("Length"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Length을 갖는 error를 찾을 수 없습니다."));
        assertThat(lengthErrorDto.getField()).isEqualTo("nickname");

        //** Pattern Error code, field 확인
        ErrorDto patternErrorDto = patternError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("Pattern"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pattern을 갖는 error를 찾을 수 없습니다."));
        assertThat(patternErrorDto.getField()).isEqualTo("nickname");

        //** nicknameDuplication Error code, field 확인
        ErrorDto nicknameDuplicationErrorDto = nicknameDuplicationError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("nicknameDuplication"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("nicknameDuplication을 갖는 error를 찾을 수 없습니다."));
        assertThat(nicknameDuplicationErrorDto.getField()).isEqualTo("nickname");
    }

    @Test
    @DisplayName("프로필 이미지 수정 성공")
    @WithUserDetails("test1@test.com")
    public void updateProfileImgTest() throws Exception {

        //given
        String fileName = "updateProfileImg.jpeg";
        MockMultipartFile file = new MockMultipartFile("profileFile", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));

        //when
        String beforeUpdateProfileImgPath = accountRepository.findByUsername(originUsername + 1 + originEmail)
                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."))
                .getProfileImgPath();


        MvcResult mvcResult = mockMvc.perform(multipart("/account/profileImg").file(file))
                .andExpect(status().isOk())
                .andReturn();

        String profileImgPath = accountRepository.findByUsername(originUsername + 1 + originEmail)
                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."))
                .getProfileImgPath();

        //then
        assertThat(profileImgPath).isNotEqualTo(beforeUpdateProfileImgPath);
    }

    @Test
    @DisplayName("프로필 이미지 수정 실패_잘못된 formData")
    @WithUserDetails("test1@test.com")
    public void updateProfileImg_IllegalFromData_Test() throws Exception {

        //given
        String blankFileName = null; //** null인 filename
        MockMultipartFile blankFile = new MockMultipartFile("profileFile", blankFileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));

        String nonExtractFileName = "updateProfileImgjpeg"; //** 확장자가 없는 filename
        MockMultipartFile nonExtractFile = new MockMultipartFile("profileFile", nonExtractFileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));

        String onlyDotFileName = "."; //** 잘못된 형식의 filename
        MockMultipartFile onlyDotFile = new MockMultipartFile("profileFile", onlyDotFileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));

        //when
        MvcResult blankResult = mockMvc.perform(multipart("/account/profileImg").file(blankFile))
                .andExpect(status().is4xxClientError())
                .andReturn();

        MvcResult nonExtractResult = mockMvc.perform(multipart("/account/profileImg").file(nonExtractFile))
                .andExpect(status().is4xxClientError())
                .andReturn();

        MvcResult onlyDotResult = mockMvc.perform(multipart("/account/profileImg").file(onlyDotFile))
                .andExpect(status().is4xxClientError())
                .andReturn();

        FormErrorResult blankError = objectMapper.readValue(blankResult.getResponse().getContentAsString(),
                FormErrorResult.class);
        FormErrorResult nonExtractError = objectMapper.readValue(nonExtractResult.getResponse().getContentAsString(),
                FormErrorResult.class);
        FormErrorResult onlyDotError = objectMapper.readValue(onlyDotResult.getResponse().getContentAsString(),
                FormErrorResult.class);

        //then
        //** BlankFileName Error code, field 확인
        ErrorDto blankErrorDto = blankError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("BlankFileName"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("BlankFileName을 갖는 error를 찾을 수 없습니다."));
        assertThat(blankErrorDto.getField()).isEqualTo("profileFile");

        //** NonExtract Error code, field 확인
        ErrorDto nonExtractErrorDto = nonExtractError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("NonExtractFileName"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("NonExtractFileName을 갖는 error를 찾을 수 없습니다."));
        assertThat(nonExtractErrorDto.getField()).isEqualTo("profileFile");

        //** OnlyDot Error code, field 확인
        ErrorDto onlyDotErrorDto = onlyDotError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("OnlyDotFileName"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("OnlyDotFileName을 갖는 error를 찾을 수 없습니다."));
        assertThat(onlyDotErrorDto.getField()).isEqualTo("profileFile");

    }

    @Test
    @DisplayName("생년월일 수정 성공")
    @WithUserDetails("test1@test.com")
    public void updateBrithDateTest() throws Exception {

        //given
        LocalDate birthDate = LocalDate.of(1000, 10, 10);

        AccountBirthDateForm accountBirthDateForm = new AccountBirthDateForm(birthDate);

        //when
        MvcResult mvcResult = mockMvc.perform(post("/account/birthDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountBirthDateForm)))
                .andExpect(status().isOk())
                .andReturn();

        Account account = accountRepository.findByUsername(originUsername + 1 + originEmail)
                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));

        //then
        assertThat(account.getBirthDate()).isEqualTo(birthDate);
    }

    @Test
    @DisplayName("생년월일 수정 실패_잘못된 formData")
    @WithUserDetails("test1@test.com")
    public void updateBrithDate_IllegalFormData_Test() throws Exception {

        //given
        String httpMessageNotReadableBirthDate = "{\"birthDate\":\"1000-1222-01\"}";
        LocalDate notNullBirthDate = null;
        LocalDate futureBirthDate = LocalDate.of(4000, 10, 10);

        AccountBirthDateForm notNullBirthDateForm = new AccountBirthDateForm(notNullBirthDate);
        AccountBirthDateForm futureBirthDateForm = new AccountBirthDateForm(futureBirthDate);

        //when
        MvcResult httpMessageNotReadableResult = mockMvc.perform(post("/account/birthDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(httpMessageNotReadableBirthDate))
                .andExpect(status().is4xxClientError())
                .andReturn();

        MvcResult notNullResult = mockMvc.perform(post("/account/birthDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notNullBirthDateForm)))
                .andExpect(status().is4xxClientError())
                .andReturn();

        MvcResult futureBirthDateResult = mockMvc.perform(post("/account/birthDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(futureBirthDateForm)))
                .andExpect(status().is4xxClientError())
                .andReturn();

        BasicErrorResult httpMessageNotReadableError = objectMapper.readValue(
                httpMessageNotReadableResult.getResponse().getContentAsString(), BasicErrorResult.class);
        FormErrorResult notNullError = objectMapper.readValue(
                notNullResult.getResponse().getContentAsString(), FormErrorResult.class);
        FormErrorResult futureBirthDateError = objectMapper.readValue(
                futureBirthDateResult.getResponse().getContentAsString(), FormErrorResult.class);

        //then
        //** httpMessageNotReadableException 확인
        assertThat(httpMessageNotReadableError.getException())
                .isEqualTo(HttpMessageNotReadableException.class.getSimpleName());

        //** NotNull error code, field 확인
        ErrorDto notNullErrorDto = notNullError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("NotNull"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notnull을 갖는 error를 찾을 수 없습니다."));
        assertThat(notNullErrorDto.getField()).isEqualTo("birthDate");

        //** futureBirthDate error code, field 확인
        ErrorDto futureBirthDateErrorDto = futureBirthDateError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("FutureBirthDate"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("futureBirthDate를 갖는 error를 찾을 수 없습니다."));
        assertThat(futureBirthDateErrorDto.getField()).isEqualTo("birthDate");
    }

    @Test
    @DisplayName("성별 수정 성공")
    @WithUserDetails("test1@test.com")
    public void updateGenderTest() throws Exception {

        //given
        Gender gender = Gender.FEMALE;

        AccountGenderForm accountGenderForm = new AccountGenderForm(gender);

        //when
        Gender beforeGender = accountRepository.findByUsername(originUsername + 1 + originEmail)
                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."))
                .getGender();

        MvcResult mvcResult = mockMvc.perform(post("/account/gender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountGenderForm)))
                .andExpect(status().isOk())
                .andReturn();

        Gender afterGender = accountRepository.findByUsername(originUsername + 1 + originEmail)
                .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."))
                .getGender();

        //then
        assertThat(afterGender).isNotEqualTo(beforeGender);
        assertThat(afterGender).isEqualTo(gender);
    }

    @Test
    @DisplayName("성별 수정 실패_잘못된 formData")
    @WithUserDetails("test1@test.com")
    public void updateGender_IllegalFormData_Test() throws Exception {

        //given
        AccountGenderForm notNullGenderForm = new AccountGenderForm(null);
        String httpMessageNotReadableGender = "{\"gender\":\"M\"}";

        //when
        MvcResult notNullResult = mockMvc.perform(post("/account/gender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notNullGenderForm)))
                .andExpect(status().is4xxClientError())
                .andReturn();

        MvcResult httpMessageNotReadableResult = mockMvc.perform(post("/account/gender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(httpMessageNotReadableGender))
                .andExpect(status().is4xxClientError())
                .andReturn();

        FormErrorResult notNullError = objectMapper.readValue(
                notNullResult.getResponse().getContentAsString(), FormErrorResult.class);

        BasicErrorResult httpMessageNotReadableError = objectMapper.readValue(
                httpMessageNotReadableResult.getResponse().getContentAsString(), BasicErrorResult.class);

        //then
        //** NotNull Error code, field 확인
        ErrorDto notNullErrorDto = notNullError.getErrorList().stream()
                .filter(errorDto -> errorDto.getCode().equals("NotNull"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("NotNull을 갖는 error를 찾을 수 없습니다."));
        assertThat(notNullErrorDto.getField()).isEqualTo("gender");

        assertThat(httpMessageNotReadableError.getException()).isEqualTo(HttpMessageNotReadableException.class.getSimpleName());
    }
}