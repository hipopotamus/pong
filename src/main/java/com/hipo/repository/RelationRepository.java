package com.hipo.repository;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Relation;
import com.hipo.domain.entity.enums.RelationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends JpaRepository<Relation, Long> {

    @Query("select relations from Relation relations " +
            "join fetch relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'FRIEND'")
    List<Relation> findAllFriend(@Param("accountId") Long accountId);

    @EntityGraph(attributePaths = {"fromAccount", "toAccount"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select relation from Relation relation " +
            "join relation.fromAccount fromAccount " +
            "join relation.toAccount toAccount " +
            "where fromAccount.id = :accountId and relation.relationState = 'FRIEND'")
    Page<Relation> findFriends(@Param("accountId") Long accountId, Pageable pageable);

    @Query("select relations from Relation relations " +
            "join fetch relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'REQUEST'")
    List<Relation> findAllRequesting(@Param("accountId") Long accountId);

    @EntityGraph(attributePaths = {"fromAccount", "toAccount"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select relations from Relation relations " +
            "join relations.fromAccount fromAccount " +
            "join relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'REQUEST'")
    Page<Relation> findRequesting(@Param("accountId") Long accountId, Pageable pageable);

    @Query("select relations from Relation relations " +
            "join fetch relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where toAccount.id = :accountId and relations.relationState = 'REQUEST' " +
            "and not toAccount in (select toAccount from Relation relation " +
                "join relation.fromAccount fromAccount " +
                "join relation.toAccount toAccount " +
                "where fromAccount.id =:accountId and relation.relationState = 'BLOCK')")
    List<Relation> findAllWaitingRequest(@Param("accountId") Long accountId);

    @EntityGraph(attributePaths = {"fromAccount", "toAccount"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select relations from Relation relations " +
            "join relations.fromAccount fromAccount " +
            "join relations.toAccount toAccount " +
            "where toAccount.id = :accountId and relations.relationState = 'REQUEST' " +
            "and not toAccount in (select toAccount from Relation relation " +
                "join relation.fromAccount fromAccount " +
                "join relation.toAccount toAccount " +
                "where fromAccount.id =:accountId and relation.relationState = 'BLOCK')")
    Page<Relation> findWaitingRequests(@Param("accountId") Long accountId, Pageable pageable);

    @Query("select relations from Relation relations " +
            "join fetch relations.fromAccount fromAccount " +
            "join fetch relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'BLOCK'")
    List<Relation> findAllBlockAccount(@Param("accountId") Long accountId);

    @EntityGraph(attributePaths = {"fromAccount", "toAccount"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select relations from Relation relations " +
            "join relations.fromAccount fromAccount " +
            "join relations.toAccount toAccount " +
            "where fromAccount.id = :accountId and relations.relationState = 'BLOCK'")
    Page<Relation> findBlockAccounts(@Param("accountId") Long accountId, Pageable pageable);

    Optional<Relation> findByFromAccountAndToAccountAndRelationStateEquals(Account fromAccount, Account toAccount,
                                                                           RelationState relationState);

    boolean existsByFromAccountAndToAccountAndRelationStateEquals(Account fromAccount, Account toAccount,
                                                                  RelationState relationState);

    Optional<Relation> findByFromAccountAndToAccount(Account fromAccount, Account toAccount);
}
