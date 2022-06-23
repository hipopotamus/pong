package com.hipo.repository;

import com.hipo.domain.entity.ChatRoom;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {

    QueryResults<ChatRoom> findChatRoomByPage(Long accountId, Pageable pageable);
}
