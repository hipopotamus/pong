package com.hipo.repository;

import com.hipo.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {

    @Query("select distinct chatRoom from AccountChatRoom accountChatRoom " +
            "join accountChatRoom.account account " +
            "join fetch accountChatRoom.chatRoom chatRoom " +
            "join fetch chatRoom.participants participants " +
            "join fetch participants.account chatMember " +
            "where account.id = :accountId")
    List<ChatRoom> findAllChatRoom(@Param("accountId") Long AccountId);
}
