package com.hipo.repository;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Relation;
import com.hipo.domain.entity.enums.RelationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends JpaRepository<Relation, Long>, RelationRepositoryCustom {

    @Query("select relations from Relation relations " +
            "join relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'REQUEST'")
    List<Relation> findAllRequest(@Param("accountId") Long accountId);

    @Query("select relations from Relation relations " +
            "join relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'FRIEND'")
    List<Relation> findAllFriend(@Param("accountId") Long accountId);

    @Query("select relations from Relation relations " +
            "join relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'BLOCK'")
    List<Relation> findAllBlock(@Param("accountId") Long accountId);

    Optional<Relation> findByFromAccountAndToAccountAndRelationStateEquals(Account fromAccount, Account toAccount,
                                                                           RelationState relationState);

    boolean existsByFromAccountAndToAccountAndRelationStateEquals(Account fromAccount, Account toAccount,
                                                                  RelationState relationState);

    Optional<Relation> findByFromAccountAndToAccount(Account fromAccount, Account toAccount);
}
