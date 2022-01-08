package com.hipo.repository;

import com.hipo.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    Optional<Account> findByNickname(String nickname);
}
