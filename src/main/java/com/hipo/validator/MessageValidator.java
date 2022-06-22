package com.hipo.validator;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.exception.IllegalRequestException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountChatRoomRepository;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageValidator {

    private final AccountRepository accountRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AccountChatRoomRepository accountChatRoomRepository;

    public void isChatRoomMember(Long accountId, Long chatRoomId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 ChatRoom을 찾을 수 없습니다."));

        if (!accountChatRoomRepository.existsByAccountAndChatRoom(account, chatRoom)) {
            throw new IllegalRequestException("해당 Account는 ChatRoom의 멤버가 아닙니다.");
        }

    }
}
