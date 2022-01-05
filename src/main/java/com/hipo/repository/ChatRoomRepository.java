package com.hipo.repository;

import com.hipo.domain.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select chatRoom from AccountChatRoom accountChatRoom " +
            "join accountChatRoom.account account " +
            "join accountChatRoom.chatRoom chatRoom " +
            "where account.id = :accountId")
    Page<ChatRoom> findChatRoom(@Param("accountId") Long accountId, Pageable pageable);

    @Query("select chatRoom from AccountChatRoom accountChatRoom " +
            "join accountChatRoom.account account " +
            "join accountChatRoom.chatRoom chatRoom " +
            "where account.id = :accountId")
    List<ChatRoom> findAllChatRoom(@Param("accountId") Long AccountId);
}
