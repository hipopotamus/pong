package com.hipo.service;

import com.hipo.dataobjcet.dto.AccountDto;
import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Relation;
import com.hipo.domain.entity.enums.RelationState;
import com.hipo.domain.processor.JudgeProcessor;
import com.hipo.exception.DuplicationRequestException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.listener.event.FriendAcceptEvent;
import com.hipo.listener.event.FriendRequestEvent;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.RelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationService {

    private final AccountRepository accountRepository;
    private final RelationRepository relationRepository;
    private final JudgeProcessor judgeProcessor;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Relation requestFriend(Long fromAccountId, Long toAccountId) {

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));

        judgeProcessor.isNotBlockRelation(fromAccount, toAccount);
        judgeProcessor.isNotSelfRequest(fromAccount, toAccount);
        judgeProcessor.isNotFriendRelation(fromAccount, toAccount);
        judgeProcessor.isFirstFriendRequest(fromAccount, toAccount);
        judgeProcessor.isFirstFriendRequest(toAccount, fromAccount);

        Relation request = Relation.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .relationState(RelationState.REQUEST)
                .build();

        eventPublisher.publishEvent(new FriendRequestEvent(request));

        return relationRepository.save(request);
    }

    @Transactional
    public void rejectRequest(Long fromAccountId, Long toAccountId) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        Relation relation = relationRepository
                .findByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.REQUEST)
                .orElseThrow(() -> new NonExistResourceException("해당 친구 요청을 찾을 수 없습니다."));

        eventPublisher.publishEvent(new FriendRequestEvent(relation));

        relation.softDelete();
    }

    @Transactional
    public void acceptFriend(Long acceptAccountId, Long requestAccountId) {

        Account acceptAccount = accountRepository.findById(acceptAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));
        Account requestAccount = accountRepository.findById(requestAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));

        judgeProcessor.isNotBlockRelation(acceptAccount, requestAccount);

        Relation requestingRelation = relationRepository
                .findByFromAccountAndToAccountAndRelationStateEquals(requestAccount, acceptAccount, RelationState.REQUEST)
                .orElseThrow(() -> new NonExistResourceException("해당 fromAccount를 갖는 Relation을 찾을 수 없습니다."));

        requestingRelation.acceptedRequest();

        eventPublisher.publishEvent(new FriendAcceptEvent(requestingRelation));

        boolean isFriend = relationRepository.existsByFromAccountAndToAccountAndRelationStateEquals(acceptAccount, requestAccount, RelationState.FRIEND);

        if (!isFriend) {
            Relation friend = Relation.builder()
                    .fromAccount(acceptAccount)
                    .toAccount(requestAccount)
                    .relationState(RelationState.FRIEND)
                    .build();

            relationRepository.save(friend);
        }
    }

    @Transactional
    public void block(Long fromAccountId, Long toAccountId) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        Optional<Relation> OptionalRelation = relationRepository.findByFromAccountAndToAccount(fromAccount, toAccount);
        if (!OptionalRelation.isPresent()) {
            Relation blockRelation = Relation.builder()
                    .fromAccount(fromAccount)
                    .toAccount(toAccount)
                    .relationState(RelationState.BLOCK)
                    .build();
            relationRepository.save(blockRelation);
            return;
        }

        Relation relation = OptionalRelation.get();
        if (relation.getRelationState() == RelationState.BLOCK) {
            throw new DuplicationRequestException("이미 차단된 상대 입니다.");
        }
        if (relation.getRelationState() == RelationState.REQUEST || relation.getRelationState() == RelationState.FRIEND) {
            relation.block();
        }
    }

    public Iterable<AccountDto> findBlockAccounts(Long accountId, Pageable pageable, boolean all) {

        if (all) {
            return relationRepository.findAllBlockAccount(accountId).stream()
                    .map(relation -> new AccountDto(relation.getToAccount()))
                    .collect(Collectors.toList());
        }

        Page<Relation> blockAccounts = relationRepository.findBlockAccounts(accountId, pageable);

        List<AccountDto> accountDtoList = blockAccounts.stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());

        return new PageImpl<>(accountDtoList, pageable, blockAccounts.getTotalElements());
    }

    public Iterable<AccountDto> findFriends(Long accountId, Pageable pageable, boolean all) {

        if (all) {
            return relationRepository.findAllFriend(accountId).stream()
                    .map(relation -> new AccountDto(relation.getToAccount()))
                    .collect(Collectors.toList());
        }

        Page<Relation> friends = relationRepository.findFriends(accountId, pageable);

        List<AccountDto> accountDtoList = friends.stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());

        return new PageImpl<>(accountDtoList, pageable, friends.getTotalElements());

    }

    public Iterable<AccountDto> findRequests(Long accountId, Pageable pageable, boolean all) {

        if (all) {
            return relationRepository.findAllRequesting(accountId).stream()
                    .map(relation -> new AccountDto(relation.getToAccount()))
                    .collect(Collectors.toList());
        }

        Page<Relation> requesting = relationRepository.findRequesting(accountId, pageable);

        List<AccountDto> accountDtoList = requesting.stream()
                .map(relation -> new AccountDto(relation.getToAccount()))
                .collect(Collectors.toList());

        return new PageImpl<>(accountDtoList, pageable, requesting.getTotalElements());
    }

    public Iterable<AccountDto> findWaitingRequests(Long accountId, Pageable pageable, boolean all) {

        if (all) {
            return relationRepository.findAllWaitingRequest(accountId).stream()
                    .map(relation -> new AccountDto(relation.getFromAccount()))
                    .collect(Collectors.toList());
        }

        Page<Relation> waitingRequests = relationRepository.findWaitingRequests(accountId, pageable);

        List<AccountDto> accountDtoList = waitingRequests.stream()
                .map(relation -> new AccountDto(relation.getFromAccount()))
                .collect(Collectors.toList());

        return new PageImpl<>(accountDtoList, pageable, waitingRequests.getTotalElements());
    }
}
