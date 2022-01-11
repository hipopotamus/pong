package com.hipo.interceptor;

import com.hipo.domain.UserAccount;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.domain.processor.JudgeProcessor;
import com.hipo.domain.processor.JwtProcessor;
import com.hipo.exception.NonExistResourceException;
import com.hipo.properties.JwtProperties;
import com.hipo.repository.AccountChatRoomRepository;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.ChatRoomRepository;
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
    private final AccountChatRoomRepository accountChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JudgeProcessor judgeProcessor;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwtHeader = accessor.getFirstNativeHeader("Authorization");
            long roomId = Long.parseLong(accessor.getNativeHeader("RoomId").get(0));

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
            ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new NonExistResourceException("해당 id을 갖는 ChatRoom을 찾을 수 없습니다."));

            judgeProcessor.isChatRoomMember(account, chatRoom);

            UserAccount userAccount = new UserAccount(account);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userAccount, null, userAccount.getAuthorities());

            accessor.setUser(authentication);
        }
        return message;
    }
}
