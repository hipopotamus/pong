package com.hipo.service;

import com.hipo.security.UserAccount;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Role;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        return createOauthPrincipal(oAuth2User, provider);
    }

    private UserAccount createOauthPrincipal(OAuth2User oAuth2User, String name) {
        if (name.equals("google")) {
            return ofGoogle(oAuth2User, name);
        }
        throw new RuntimeException();
    }

    private UserAccount ofGoogle(OAuth2User oAuth2User, String name) {
        String username = name + "_" + oAuth2User.getAttribute("email");
        Optional<Account> findAccount = accountRepository.findByUsername(username);

        if (findAccount.isEmpty()) {
            String password = bCryptPasswordEncoder.encode("HipoService_OAuthAccount");
            String profileImgName = "default.jpeg";
            String nickname = UUID.randomUUID().toString();

            Account account = Account.builder()
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .role(Role.User)
                    .profileImgName(profileImgName)
                    .build();
            Account savedAccount = accountRepository.save(account);
            return new UserAccount(savedAccount, oAuth2User.getAttributes());
        }
        return new UserAccount(findAccount.get(), oAuth2User.getAttributes());

    }
}
