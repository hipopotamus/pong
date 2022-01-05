package com.hipo.service;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.AccountChatRoom;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.domain.entity.enums.RelationState;
import com.hipo.exception.IllegalRequestException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountChatRoomRepository;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.ChatRoomRepository;
import com.hipo.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountChatRoomService {

    private final AccountChatRoomRepository accountChatRoomRepository;
    private final AccountRepository accountRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RelationRepository relationRepository;

    @Transactional
    public AccountChatRoom createAccountChatRoom(Long inviteAccountId, Long acceptAccountId, Long chatRoomId) {
        Account inviteAccount = accountRepository.findById(inviteAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        Account acceptAccount = accountRepository.findById(acceptAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        if (!relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(inviteAccount, acceptAccount
                , RelationState.FRIEND)) {
            throw new IllegalRequestException("친구 관계가 아닙니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 ChatRoom을 찾을 수 없습니다."));

        return accountChatRoomRepository.save(new AccountChatRoom(acceptAccount, chatRoom));
    }
}
