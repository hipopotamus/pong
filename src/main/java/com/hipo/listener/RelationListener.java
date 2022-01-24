package com.hipo.listener;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.Notification;
import com.hipo.domain.entity.enums.NotificationType;
import com.hipo.listener.event.FriendAcceptEvent;
import com.hipo.listener.event.FriendRejectEvent;
import com.hipo.listener.event.FriendRequestEvent;
import com.hipo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RelationListener {

    private final NotificationService notificationService;

    @Transactional
    @EventListener
    public void handleFriendRequestEvent(FriendRequestEvent friendRequestEvent) {
        Account fromAccount = friendRequestEvent.getRelation().getFromAccount();
        Account toAccount = friendRequestEvent.getRelation().getToAccount();
        String message = fromAccount.getNickname() + "님이 친구 요청을 하였습니다.";

        Notification notification = notificationService.createNotification(message, NotificationType.FRIEND_REQUEST, toAccount.getId());
        notificationService.sendNotificationToSocket(toAccount, notification);
    }

    @Transactional
    @EventListener
    public void handleFriendRejectEvent(FriendRejectEvent friendRejectEvent) {
        Account fromAccount = friendRejectEvent.getRelation().getFromAccount();
        Account toAccount = friendRejectEvent.getRelation().getToAccount();
        String message = toAccount.getNickname() + "님이 친구 요청을 거절 하였습니다.";

        Notification notification = notificationService.createNotification(message, NotificationType.FRIEND_ACCEPT, fromAccount.getId());
        notificationService.sendNotificationToSocket(fromAccount, notification);
    }

    @Transactional
    @EventListener
    public void handleFriendAcceptEvent(FriendAcceptEvent friendAcceptEvent) {
        Account fromAccount = friendAcceptEvent.getRelation().getFromAccount();
        Account toAccount = friendAcceptEvent.getRelation().getToAccount();
        String message = toAccount.getNickname() + "님이 친구 요청을 수락 하였습니다.";

        Notification notification = notificationService.createNotification(message, NotificationType.FRIEND_REJECT, fromAccount.getId());
        notificationService.sendNotificationToSocket(fromAccount, notification);
    }

}
