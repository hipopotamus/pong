package com.hipo.repository;

import com.hipo.dataobjcet.dto.NotificationSearchCond;
import com.hipo.domain.entity.Notification;
import com.hipo.domain.entity.enums.NotificationType;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.hipo.domain.entity.QAccount.account;
import static com.hipo.domain.entity.QNotification.notification;

public class NotificationRepositoryImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public NotificationRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Notification> findAllNotification(Long accountId, NotificationSearchCond notificationSearchCond) {
        return jpaQueryFactory
                .selectFrom(notification)
                .join(notification.account, account)
                .where(notification.account.id.eq(accountId),
                        notificationTypeEq(notificationSearchCond.getNotificationType()),
                        checkedEq(notificationSearchCond.isChecked()))
                .orderBy(notification.createDate.desc())
                .fetch();
    }

    @Override
    public QueryResults<Notification> findNotifications(Long accountId, NotificationSearchCond notificationSearchCond,
                                                        Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(notification)
                .join(notification.account, account)
                .where(notification.account.id.eq(accountId),
                        notificationTypeEq(notificationSearchCond.getNotificationType()),
                        checkedEq(notificationSearchCond.isChecked()))
                .orderBy(notification.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
    }

    private BooleanExpression notificationTypeEq(NotificationType notificationType) {
        return notificationType != null ? notification.notificationType.eq(notificationType) : null;
    }

    private BooleanExpression checkedEq(boolean checked) {
        return notification.checked.eq(checked);
    }
}
