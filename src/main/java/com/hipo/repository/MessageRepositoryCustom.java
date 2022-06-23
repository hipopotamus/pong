package com.hipo.repository;

import com.hipo.domain.entity.Message;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageRepositoryCustom {

    QueryResults<Message> findChatRoomMessageBySlice(Long accountId, Long chatRoomId, Pageable pageable);

    List<Message> findAllChatRoomMessage(Long accountId, Long chatRoomId);
}
