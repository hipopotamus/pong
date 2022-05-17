package com.hipo.repository;

import com.hipo.domain.entity.Account;
import com.hipo.web.form.AccountSearchCond;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Pageable;

public interface AccountRepositoryCustom {
    public QueryResults<Account> findAccountsByPage(AccountSearchCond accountSearchCond, Pageable pageable);
}
