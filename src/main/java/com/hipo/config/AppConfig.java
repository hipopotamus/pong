package com.hipo.config;

import com.hipo.domain.UserAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {

                //** test용
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    return Optional.of("testUser");
                }
                //** test용

                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal == "anonymousUser") {
                    return Optional.of(principal.toString());
                }

                return Optional.of(((UserAccount) principal).getAccount().getNickname());
            }
        };
    }
}
