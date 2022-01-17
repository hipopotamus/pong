package com.hipo.repository;

import com.hipo.domain.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select message from Message message " +
            "join fetch message.chatRoom chatRoom " +
            "join fetch message.account account " +
            "where chatRoom.id = :chatRoomId " +
            "and not account in (select toAccount from Relation relation " +
                "join relation.fromAccount fromAccount " +
                "join relation.toAccount toAccount " +
                "where fromAccount.id =:accountId and relation.relationState = 'BLOCK') " +
            "order by message.createDate")
    Slice<Message> findChatRoomMessage(@Param("accountId") Long accountId, @Param("chatRoomId") Long chatRoomId,
                                       Pageable pageable);

    @Query("select message from Message message " +
            "join fetch message.chatRoom chatRoom " +
            "join fetch message.account account " +
            "where chatRoom.id = :chatRoomId " +
            "and not account in (select toAccount from Relation relation " +
                "join relation.fromAccount fromAccount " +
                "join relation.toAccount toAccount " +
                "where fromAccount.id =:accountId and relation.relationState = 'BLOCK') " +
            "order by message.createDate")
    List<Message> findAllChatRoomMessage(@Param("accountId") Long accountId, @Param("chatRoomId") Long chatRoomId);

}
