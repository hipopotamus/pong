package com.hipo.service;

import com.hipo.dataobjcet.dto.AccountDto;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Relation;
import com.hipo.domain.entity.enums.RelationState;
import com.hipo.exception.NonExistResourceException;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationService {

    private final AccountRepository accountRepository;
    private final RelationRepository relationRepository;

    @Transactional
    public Relation requestFriend(Long fromAccountId, Long toAccountId) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));

        return Relation.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .relationState(RelationState.REQUEST)
                .build();
    }

    @Transactional
    public Relation acceptFriend(Long acceptAccountId, Long requestAccountId) {
        Relation requestingRelation = relationRepository.findByFromAccount(acceptAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 fromAccount를 갖는 Relation을 찾을 수 없습니다."));

        requestingRelation.acceptedRequest();

        Account fromAccount = accountRepository.findById(acceptAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(requestAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));

        return Relation.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .relationState(RelationState.FRIEND)
                .build();
    }

    public List<AccountDto> findFriends(Long accountId) {
        List<Relation> friends = relationRepository.findFriends(accountId);

        return friends.stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
    }

    public List<AccountDto> findRequests(Long accountId) {
        List<Relation> requests = relationRepository.findRequesting(accountId);

        return requests.stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
    }

    public List<AccountDto> findWaitingRequests(Long accountId) {
        List<Relation> waitingRequests = relationRepository.findWaitingRequests(accountId);

        return waitingRequests.stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());
    }
}
