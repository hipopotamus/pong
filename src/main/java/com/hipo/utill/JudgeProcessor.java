package com.hipo.utill;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.domain.entity.enums.RelationState;
import com.hipo.exception.DuplicationRequestException;
import com.hipo.exception.IllegalRequestException;
import com.hipo.repository.AccountChatRoomRepository;
import com.hipo.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JudgeProcessor {

    private final AccountChatRoomRepository accountChatRoomRepository;
    private final RelationRepository relationRepository;

    public boolean isChatRoomMember(Account account, ChatRoom chatRoom) {
        if (!accountChatRoomRepository.existsByAccountAndChatRoom(account, chatRoom)) {
            throw new IllegalRequestException("해당 Account는 채팅룸에 속해있지 않습니다.");
        }
        return true;
    }

    public boolean isFriendRelation(Account inviteAccount, Account acceptAccount) {
        if (!relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(inviteAccount, acceptAccount
                , RelationState.FRIEND)) {
            throw new IllegalRequestException("친구 관계가 아닙니다.");
        }
        return true;
    }

    public boolean isFirstFriendRequest(Account fromAccount, Account toAccount) {
        if (relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount,
                RelationState.REQUEST)) {
            throw new DuplicationRequestException("이미 존재하는 요청입니다.");
        }
        return true;
    }

    public boolean isNotFriendRelation(Account inviteAccount, Account acceptAccount) {
        if (relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(inviteAccount, acceptAccount
                , RelationState.FRIEND)) {
            throw new IllegalRequestException("이미 친구 관계입니다.");
        }
        return true;
    }

    public boolean isNotSelfRequest(Account fromAccount, Account toAccount) {
        if (fromAccount.equals(toAccount)) {
            throw new IllegalRequestException("자기자신에 대한 요청입니다.");
        }
        return true;
    }


    public boolean isNotBlockRelation(Account fromAccount, Account toAccount) {
        if (relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount
                , RelationState.BLOCK)) {
            throw new IllegalRequestException("차단된 회원입니다.");
        }
        return true;
    }
}