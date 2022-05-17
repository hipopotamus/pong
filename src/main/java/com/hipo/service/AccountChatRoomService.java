package com.hipo.service;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.AccountChatRoom;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.utill.JudgeProcessor;
import com.hipo.exception.NonExistResourceException;
import com.hipo.listener.event.InviteChatRoomEvent;
import com.hipo.repository.AccountChatRoomRepository;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountChatRoomService {

    private final AccountChatRoomRepository accountChatRoomRepository;
    private final AccountRepository accountRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final JudgeProcessor judgeProcessor;

    @Transactional
    public AccountChatRoom createAccountChatRoom(Long inviteAccountId, Long acceptAccountId, Long chatRoomId) {
        Account inviteAccount = accountRepository.findById(inviteAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        Account acceptAccount = accountRepository.findById(acceptAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        judgeProcessor.isFriendRelation(inviteAccount, acceptAccount);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 ChatRoom을 찾을 수 없습니다."));

        eventPublisher.publishEvent(new InviteChatRoomEvent(inviteAccount, acceptAccount, chatRoom));

        return accountChatRoomRepository.save(new AccountChatRoom(acceptAccount, chatRoom));
    }
}
