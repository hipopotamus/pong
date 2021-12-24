package com.hipo.domain.init;

import com.hipo.domain.entity.enums.Gender;
import com.hipo.service.AccountService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;

@SpringBootTest
public class BasicInit {

    @Autowired
    AccountService accountService;

    @Test
    public void initTest() throws IOException {
        String fileName = "hipo.jpeg";
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/main/resources/static/sample/ea451747-9643" +
                        "-470f-9605-81407fc077c7.jpeg"));

        for (int i = 1; i < 51; i++) {
            accountService.createAccount("test" + i + "@naver.com", "1234", "testNickname" + i,
                    file, Gender.MAN, LocalDate.now());
        }
    }
}
