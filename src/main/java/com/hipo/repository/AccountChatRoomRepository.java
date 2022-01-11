package com.hipo.repository;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.AccountChatRoom;
import com.hipo.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountChatRoomRepository extends JpaRepository<AccountChatRoom, Long> {

    boolean existsByAccountAndChatRoom(Account account, ChatRoom chatRoom);
}
