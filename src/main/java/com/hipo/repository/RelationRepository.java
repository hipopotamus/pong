package com.hipo.repository;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Relation;
import com.hipo.domain.entity.enums.RelationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends JpaRepository<Relation, Long> {

    Optional<Relation> findByFromAccountAndToAccountAndRelationStateEquals(Account fromAccount, Account toAccount,
                                                                           RelationState relationState);

    @Query("select relations from Relation relations " +
            "join fetch relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'FRIEND'")
    List<Relation> findFriends(@Param("accountId") Long accountId);

    @Query("select relations from Relation relations " +
            "join fetch relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'REQUEST'")
    List<Relation> findRequesting(@Param("accountId") Long accountId);

    @Query("select relations from Relation relations " +
            "join fetch relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where toAccount.id = :accountId and relations.relationState = 'REQUEST'")
    List<Relation> findWaitingRequests(@Param("accountId") Long accountId);

    boolean existsByFromAccountAndToAccountAndRelationStateEquals(Account fromAccount, Account toAccount,
                                                                  RelationState relationState);
}
