package com.hipo.test;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.Gender;
import com.hipo.domain.entity.enums.Role;
import com.hipo.service.AccountService;
import com.hipo.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TestInit {

    private final InitService initService;

//    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {

        private final AccountService accountService;
        private final RelationService relationService;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        public void init() {
            //make Account
            for (int i = 1; i <= 20; i++) {
                Account account = Account.builder()
                        .username("test" + i + "@test.com")
                        .password(bCryptPasswordEncoder.encode("1234"))
                        .nickname("test" + i + "Nickname")
                        .profileImgName("default.jpeg")
                        .role(Role.User)
                        .gender(Gender.MAN)
                        .birthDate(LocalDate.now())
                        .build();
                accountService.saveAccount(account);
            }

            //make Relation
            for (int i = 2; i <= 20; i++) {
                Account fromAccount = accountService.findByNickname("test1Nickname");
                Account toAccount = accountService.findByNickname("test" + i + "Nickname");
                relationService.requestFriend(fromAccount.getId(), toAccount.getId());
                relationService.acceptFriend(toAccount.getId(), fromAccount.getId());
            }

            //test20Nickname is Blocked
            Account fromAccount = accountService.findByNickname("test1Nickname");
            Account toAccount = accountService.findByNickname("test20Nickname");
            relationService.block(fromAccount.getId(), toAccount.getId());
        }
    }

}
