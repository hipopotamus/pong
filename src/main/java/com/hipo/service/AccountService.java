package com.hipo.service;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(String username, String password, String nickname, MultipartFile multipartFile,
                                 Gender gender, LocalDate birthDate) {
        return null;
    }

}
