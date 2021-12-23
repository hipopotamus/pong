package com.hipo.repository;

import com.hipo.domain.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a from Account a")
    Page<Account> findAllByPage(Pageable pageable);

    @Query("select a from Account a where a.nickname like %:keyWord%")
    Page<Account> searchByPage(@Param("keyWord") String keyWord, Pageable pageable);

    Page<Account> findByNicknameIgnoreCaseContaining(String keyWord, Pageable pageable);
}
