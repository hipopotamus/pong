package com.hipo.config;

import com.hipo.security.UserAccount;
import com.p6spy.engine.spy.P6SpyOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Configuration
public class AppConfig {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spySqlFormatConfiguration.class.getName());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
