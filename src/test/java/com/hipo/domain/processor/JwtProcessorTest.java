package com.hipo.domain.processor;

import com.hipo.domain.UserAccount;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import com.hipo.properties.JwtProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.time.LocalDate;

class JwtProcessorTest {

    JwtProcessor jwtProcessor = new JwtProcessor();
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("jwt encoding, decoding 테스트")
    public void jwtCreateDecodeTest() throws IOException {
        //given
        Account account = new Account("test@test.com", bCryptPasswordEncoder.encode("1234"),
                "testNickname", "/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg",
                Role.User, Gender.MAN, LocalDate.now());

        //when
        String jwtToken = jwtProcessor.createAuthJwtToken(new UserAccount(account));
        String username = jwtProcessor.decodeJwtToken(jwtToken, JwtProperties.SECRET, "username");

        //then
        Assertions.assertThat(username).isEqualTo(account.getUsername());
    }
}