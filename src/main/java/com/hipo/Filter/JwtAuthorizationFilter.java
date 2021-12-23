package com.hipo.Filter;

import com.hipo.domain.UserAccount;
import com.hipo.domain.entity.Account;
import com.hipo.domain.processor.JwtProcessor;
import com.hipo.exception.NonExistResource;
import com.hipo.properties.JwtProperties;
import com.hipo.repository.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final AccountRepository accountRepository;
    private final JwtProcessor jwtProcessor;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AccountRepository accountRepository,
                                  JwtProcessor jwtProcessor) {
        super(authenticationManager);
        this.accountRepository = accountRepository;
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        int pos = jwtHeader.lastIndexOf(" ");
        String jwtToken = jwtHeader.substring(pos + 1);

        String username = jwtProcessor.decodeJwtToken(jwtToken, JwtProperties.SECRET, "username");

        if (username != null) {
            Account account = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new NonExistResource("해당 username을 갖는 Account를 찾을 수 없습니다."));

            UserAccount userAccount = new UserAccount(account);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount, null,
                    userAccount.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
