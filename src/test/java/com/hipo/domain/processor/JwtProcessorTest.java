package com.hipo.domain.processor;

import com.hipo.domain.UserAccount;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import com.hipo.properties.JwtProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class JwtProcessorTest {

    JwtProcessor jwtProcessor = new JwtProcessor();

    static List<Account> accountList = new ArrayList<>();

    @BeforeAll
    static void init() throws IOException {
        String fileName = "hipo.jpeg";
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/main/resources/static/sample/ea451747-9643" +
                        "-470f-9605-81407fc077c7.jpeg"));

        for (int i = 1; i < 51; i++) {
            Account account = new Account("test" + i + "@naver.com", "1234", "testNickname" + i,
                    "/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg",
                    Role.User, Gender.MAN, LocalDate.now());
            accountList.add(account);
        }
    }

    @Test
    @DisplayName("jwt encoding, decoding 테스트")
    public void jwtCreateDecodeTest() {
        List<String> usernameList = accountList.stream()
                .map(Account::getUsername)
                .collect(Collectors.toList());

        List<String> decodeUsernameList = accountList.stream()
                .map(UserAccount::new)
                .map(userAccount -> jwtProcessor.createAuthJwtToken(userAccount))
                .map(jwtToken -> jwtProcessor.decodeJwtToken(jwtToken, JwtProperties.SECRET, "username"))
                .collect(Collectors.toList());

        Assertions.assertThat(usernameList).isEqualTo(decodeUsernameList);
    }
}