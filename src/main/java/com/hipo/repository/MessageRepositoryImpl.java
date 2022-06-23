package com.hipo.repository;

import com.hipo.domain.entity.Message;
import com.hipo.domain.entity.QAccount;
import com.hipo.domain.entity.QMessage;
import com.hipo.domain.entity.enums.RelationState;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

import static com.hipo.domain.entity.QChatRoom.chatRoom;
import static com.hipo.domain.entity.QRelation.relation;

public class MessageRepositoryImpl extends QuerydslRepositorySupport implements MessageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MessageRepositoryImpl(EntityManager entityManager) {
        super(Message.class);
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public QueryResults<Message> findChatRoomMessageBySlice(Long accountId, Long chatRoomId, Pageable pageable) {
        QMessage message = new QMessage("message");
        QAccount messageAccount = new QAccount("messageAccount");
        QAccount fromAccount = new QAccount("fromAccount");
        QAccount toAccount = new QAccount("toAccount");


        return jpaQueryFactory
                .selectFrom(message)
                .join(message.chatRoom, chatRoom)
                .join(message.account, messageAccount).fetchJoin()
                .where(chatRoom.id.eq(chatRoomId),
                        messageAccount.notIn(JPAExpressions
                                .select(toAccount)
                                .from(relation)
                                .join(relation.toAccount, toAccount)
                                .join(relation.fromAccount, fromAccount)
                                .where(toAccount.id.eq(accountId),
                                        relation.relationState.eq(RelationState.BLOCK))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetchResults();
    }

    @Override
    public List<Message> findAllChatRoomMessage(Long accountId, Long chatRoomId) {
        QMessage message = new QMessage("message");
        QAccount messageAccount = new QAccount("messageAccount");
        QAccount fromAccount = new QAccount("fromAccount");
        QAccount toAccount = new QAccount("toAccount");

        return jpaQueryFactory
                .selectFrom(message)
                .join(message.chatRoom, chatRoom)
                .join(message.account, messageAccount).fetchJoin()
                .where(chatRoom.id.eq(chatRoomId),
                        messageAccount.notIn(JPAExpressions
                                .select(toAccount)
                                .from(relation)
                                .join(relation.toAccount, toAccount)
                                .join(relation.fromAccount, fromAccount)
                                .where(toAccount.id.eq(accountId),
                                        relation.relationState.eq(RelationState.BLOCK))))
                .fetch();
    }
}
