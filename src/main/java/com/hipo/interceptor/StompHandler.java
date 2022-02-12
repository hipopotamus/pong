package com.hipo.interceptor;

import com.hipo.domain.UserAccount;
import com.hipo.domain.entity.Account;
import com.hipo.domain.processor.JwtProcessor;
import com.hipo.exception.NonExistResourceException;
import com.hipo.properties.JwtProperties;
import com.hipo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtProcessor jwtProcessor;
    private final AccountRepository accountRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwtHeader = accessor.getFirstNativeHeader("Authorization");

            if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
                return message;
            }

            String jwtToken = jwtProcessor.extractBearer(jwtHeader);
            String username = jwtProcessor.decodeJwtToken(jwtToken, JwtProperties.SECRET, "username");

            if (username == null) {
                return message;
            }

            Account account = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new NonExistResourceException("해당 username을 갖는 Account를 찾을 수 없습니다."));

            UserAccount userAccount = new UserAccount(account);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userAccount, null, userAccount.getAuthorities());

            accessor.setUser(authentication);
        }
        return message;
    }
}
