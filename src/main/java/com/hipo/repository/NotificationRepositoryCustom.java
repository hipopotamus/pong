package com.hipo.repository;

import com.hipo.web.dto.NotificationSearchCond;
import com.hipo.domain.entity.Notification;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationRepositoryCustom {

    public List<Notification> findAllNotification(Long accountId, NotificationSearchCond notificationSearchCond);

    public QueryResults<Notification> findNotifications(Long accountId, NotificationSearchCond notificationSearchCond,
                                                        Pageable pageable);
}
