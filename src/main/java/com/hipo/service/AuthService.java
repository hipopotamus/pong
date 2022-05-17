package com.hipo.service;

import com.hipo.security.UserAccount;
import com.hipo.domain.processor.AuthProcessor;
import com.hipo.domain.processor.JwtProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthProcessor authProcessor;
    private final JwtProcessor jwtProcessor;

    public String login(String username, String password) {
        UserAccount userAccount = (UserAccount) authProcessor.authenticateAccount(username, password);
        return jwtProcessor.createAuthJwtToken(userAccount);
    }

}
