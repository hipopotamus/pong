package com.hipo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountControllerTest {

    @Test
    public void createAccountTest() {

    }

    @Test
    @WithUserDetails("test1@naver.com")
    public void updateAccountNicknameTest() {

    }

}