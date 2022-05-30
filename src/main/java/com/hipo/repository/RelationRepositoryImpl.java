package com.hipo.repository;

import com.hipo.domain.entity.QAccount;
import com.hipo.domain.entity.Relation;
import com.hipo.domain.entity.enums.RelationState;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import static com.hipo.domain.entity.QRelation.relation;

public class RelationRepositoryImpl extends QuerydslRepositorySupport implements RelationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public RelationRepositoryImpl(EntityManager entityManager) {
        super(Relation.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public QueryResults<Relation> findRequestsByPage(Long accountId, Pageable pageable) {
        QAccount fromAccount = new QAccount("fromAccount");
        QAccount toAccount = new QAccount("toAccount");

        JPAQuery<Relation> query = jpaQueryFactory.selectFrom(relation)
                .join(relation.fromAccount, fromAccount)
                .join(relation.toAccount, toAccount).fetchJoin()
                .where(fromAccount.id.eq(accountId), relation.relationState.eq(RelationState.REQUEST));
        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        return query.fetchResults();
    }

    @Override
    public List<Relation> findAllRequestFromOther(Long accountId) {
        QAccount fromAccount = new QAccount("fromAccount");
        QAccount toAccount = new QAccount("toAccount");
        QAccount inFromAccount = new QAccount("inFromAccount");
        QAccount inToAccount = new QAccount("inToAccount");

        return jpaQueryFactory.selectFrom(relation)
                .join(relation.fromAccount, fromAccount).fetchJoin()
                .join(relation.toAccount, toAccount)
                .where(toAccount.id.eq(accountId),
                        relation.relationState.eq(RelationState.REQUEST),
                        fromAccount.notIn(JPAExpressions.select(inToAccount)
                                .from(relation)
                                .join(relation.fromAccount, inFromAccount)
                                .join(relation.toAccount, inToAccount)
                                .where(inFromAccount.id.eq(accountId), relation.relationState.eq(RelationState.BLOCK))))
                .fetch();
    }

    @Override
    public QueryResults<Relation> findRequestsFromOtherByPage(Long accountId, Pageable pageable) {
        QAccount fromAccount = new QAccount("fromAccount");
        QAccount toAccount = new QAccount("toAccount");
        QAccount inFromAccount = new QAccount("inFromAccount");
        QAccount inToAccount = new QAccount("inToAccount");

        JPAQuery<Relation> query = jpaQueryFactory.selectFrom(relation)
                .join(relation.fromAccount, fromAccount).fetchJoin()
                .join(relation.toAccount, toAccount)
                .where(toAccount.id.eq(accountId),
                        relation.relationState.eq(RelationState.REQUEST),
                        fromAccount.notIn(JPAExpressions.select(inToAccount)
                                .from(relation)
                                .join(relation.fromAccount, inFromAccount)
                                .join(relation.toAccount, inToAccount)
                                .where(inFromAccount.id.eq(accountId),
                                        relation.relationState.eq(RelationState.BLOCK))));
        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        return query.fetchResults();
    }

    @Override
    public QueryResults<Relation> findFriendsByPage(Long accountId, Pageable pageable) {
        QAccount fromAccount = new QAccount("fromAccount");
        QAccount toAccount = new QAccount("toAccount");

        JPAQuery<Relation> query = jpaQueryFactory.selectFrom(relation)
                .join(relation.fromAccount, fromAccount)
                .join(relation.toAccount, toAccount).fetchJoin()
                .where(fromAccount.id.eq(accountId), relation.relationState.eq(RelationState.FRIEND));
        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        return query.fetchResults();
    }

    @Override
    public QueryResults<Relation> findBlocksByPage(Long accountId, Pageable pageable) {
        QAccount fromAccount = new QAccount("fromAccount");
        QAccount toAccount = new QAccount("toAccount");

        JPAQuery<Relation> query = jpaQueryFactory.selectFrom(relation)
                .join(relation.fromAccount, fromAccount)
                .join(relation.toAccount, toAccount).fetchJoin()
                .where(fromAccount.id.eq(accountId), relation.relationState.eq(RelationState.BLOCK));
        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        return query.fetchResults();
    }


}
