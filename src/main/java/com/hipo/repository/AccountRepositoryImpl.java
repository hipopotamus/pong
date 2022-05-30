package com.hipo.repository;

import com.hipo.domain.entity.Account;
import com.hipo.web.form.AccountSearchCond;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.Objects;

import static com.hipo.domain.entity.QAccount.account;

public class AccountRepositoryImpl extends QuerydslRepositorySupport implements AccountRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public AccountRepositoryImpl(EntityManager entityManager) {
        super(Account.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public QueryResults<Account> findAccountByPage(AccountSearchCond accountSearchCond, Pageable pageable) {
        JPAQuery<Account> query = jpaQueryFactory
                .selectFrom(account)
                .where(AccountNicknameContain(accountSearchCond.getNickname()));

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);
        return query.fetchResults();
    }

    private BooleanExpression AccountNicknameContain(String nickname) {
        return nickname != null ? account.nickname.contains(nickname) : null;
    }
}
