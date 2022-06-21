package com.hipo.service;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Relation;
import com.hipo.domain.entity.enums.RelationState;
import com.hipo.exception.NonExistResourceException;
import com.hipo.listener.event.FriendAcceptEvent;
import com.hipo.listener.event.FriendRequestEvent;
import com.hipo.repository.AccountRepository;
import com.hipo.repository.RelationRepository;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationService {

    private final AccountRepository accountRepository;
    private final RelationRepository relationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Relation requestFriend(Long fromAccountId, Long toAccountId) {

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));

        Relation request = Relation.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .relationState(RelationState.REQUEST)
                .build();

        eventPublisher.publishEvent(new FriendRequestEvent(request));

        return relationRepository.save(request);
    }

    @Transactional
    public void acceptFriend(Long fromAccountId, Long toAccountId) {

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 Id를 갖는 Account를 찾을 수 없습니다."));

        Relation requestingRelation = relationRepository
                .findByFromAccountAndToAccountAndRelationStateEquals(fromAccount, toAccount, RelationState.REQUEST)
                .orElseThrow(() -> new NonExistResourceException("해당 fromAccount를 갖는 Relation을 찾을 수 없습니다."));

        requestingRelation.makeFriend();

        eventPublisher.publishEvent(new FriendAcceptEvent(requestingRelation));

        Optional<Relation> optionalRelation = relationRepository.findByFromAccountAndToAccount(toAccount, fromAccount);
        if (optionalRelation.isEmpty()) {
            Relation friend = Relation.builder()
                    .fromAccount(fromAccount)
                    .toAccount(toAccount)
                    .relationState(RelationState.FRIEND)
                    .build();

            relationRepository.save(friend);
            return;
        }
        optionalRelation.get().makeFriend();
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
    public void block(Long fromAccountId, Long toAccountId) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        Optional<Relation> optionalRelation = relationRepository.findByFromAccountAndToAccount(fromAccount, toAccount);
        if (optionalRelation.isEmpty()) {
            Relation blockRelation = Relation.builder()
                    .fromAccount(fromAccount)
                    .toAccount(toAccount)
                    .relationState(RelationState.BLOCK)
                    .build();
            relationRepository.save(blockRelation);
            return;
        }

        optionalRelation.get().block();
    }

    @Transactional
    public void unBlock(Long fromAccountId, Long toAccountId) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new NonExistResourceException("해당 id를 갖는 Account를 찾을 수 없습니다."));

        Relation relation = relationRepository.findByFromAccountAndToAccountAndRelationStateEquals(fromAccount,
                        toAccount, RelationState.BLOCK)
                .orElseThrow(() -> new NonExistResourceException("해당 Account의 Block된 Relation을 찾을 수 없습니다."));
        relation.softDelete();
    }

    public List<Relation> findAllRequest(Long accountId) {
        return relationRepository.findAllRequest(accountId);
    }

    public QueryResults<Relation> findRequestsByPage(Long accountId, Pageable pageable) {
        return relationRepository.findRequestsByPage(accountId, pageable);
    }

    public List<Relation> findAllRequestFromOther(Long accountId) {
        return relationRepository.findAllRequestFromOther(accountId);
    }

    public QueryResults<Relation> findRequestsFromOtherByPage(Long accountId, Pageable pageable) {
        return relationRepository.findRequestsFromOtherByPage(accountId, pageable);
    }

    public List<Relation> findAllFriend(Long accountId) {
        return relationRepository.findAllFriend(accountId);
    }

    public QueryResults<Relation> findFriendsByPage(Long accountId, Pageable pageable) {
        return relationRepository.findFriendsByPage(accountId, pageable);
    }

    public List<Relation> findAllBlock(Long accountId) {
        return relationRepository.findAllBlock(accountId);
    }

    public QueryResults<Relation> findBlocksByPage(Long accountId, Pageable pageable) {
        return relationRepository.findBlocksByPage(accountId, pageable);
    }
}
