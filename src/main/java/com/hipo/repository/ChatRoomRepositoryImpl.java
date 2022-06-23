package com.hipo.repository;

import com.hipo.domain.entity.ChatRoom;
import com.hipo.domain.entity.QAccount;
import com.hipo.domain.entity.QAccountChatRoom;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.Objects;

import static com.hipo.domain.entity.QAccount.account;
import static com.hipo.domain.entity.QAccountChatRoom.accountChatRoom;
import static com.hipo.domain.entity.QChatRoom.chatRoom;

public class ChatRoomRepositoryImpl extends QuerydslRepositorySupport implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ChatRoomRepositoryImpl(EntityManager entityManager) {
        super(ChatRoom.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public QueryResults<ChatRoom> findChatRoomByPage(Long accountId, Pageable pageable) {

        QAccountChatRoom accountChatRoomInChatRoom = new QAccountChatRoom("accountChatRoomInChatRoom");
        QAccount chatRoomMember = new QAccount("chatRoomMember");

        JPAQuery<ChatRoom> query = jpaQueryFactory
                .select(chatRoom)
                .distinct()
                .from(accountChatRoom)
                .join(accountChatRoom.account, account)
                .join(accountChatRoom.chatRoom, chatRoom)
                .join(chatRoom.participants, accountChatRoomInChatRoom).fetchJoin()
                .join(accountChatRoomInChatRoom.account, chatRoomMember).fetchJoin()
                .where(account.id.eq(accountId));

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query);

        return query.fetchResults();
    }
}
