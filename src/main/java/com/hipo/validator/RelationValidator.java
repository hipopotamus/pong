package com.hipo.validator;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.enums.RelationState;
import com.hipo.exception.DuplicationRequestException;
import com.hipo.exception.IllegalRequestException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RelationValidator {

    private final RelationRepository relationRepository;
    private final AccountRepository accountRepository;

    private void isBlockRelationByAccount(Account fromAccount, Account toAccount) {
        if (relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount,
                RelationState.BLOCK)) {
            throw new IllegalRequestException("차단된 회원입니다.");
        }
    }

    public void isBlockRelation(Long fromAccountId, Long toAccountId) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));

        isBlockRelationByAccount(fromAccount, toAccount);
    }

    public void validRequestFriend(Long fromAccountId, Long toAccountId) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));

        isBlockRelationByAccount(fromAccount, toAccount);
        if (relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount
                , RelationState.FRIEND)) {
            throw new IllegalRequestException("이미 친구 관계입니다.");
        }
        if (fromAccount.equals(toAccount)) {
            throw new IllegalRequestException("자기자신에 대한 요청입니다.");
        }
        if (relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount,
                RelationState.REQUEST)) {
            throw new DuplicationRequestException("이미 존재하는 요청입니다.");
        }
    }
}
