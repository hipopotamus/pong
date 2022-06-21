package com.hipo.listener;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.ChatRoom;
import com.hipo.domain.entity.Notification;
import com.hipo.domain.entity.enums.NotificationType;
import com.hipo.listener.event.InviteChatRoomEvent;
import com.hipo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountChatRoomListener {

    private final NotificationService notificationService;

    @Transactional
    @EventListener
    public void handlerInviteChatRoomEvent(InviteChatRoomEvent inviteChatRoomEvent) {
        Account inviteAccount = inviteChatRoomEvent.getInviteAccount();
        Account acceptAccount = inviteChatRoomEvent.getAcceptAccount();
        ChatRoom chatRoom = inviteChatRoomEvent.getChatRoom();

        String message = inviteAccount.getNickname() + "님이 " + "[" + chatRoom.getName() + "]" + " 채팅방에 초대하였습니다.";
        Notification notification = notificationService.createNotification(message, NotificationType.INVITE_CHATROOM, acceptAccount.getId());
        notificationService.sendNotificationToSocket(acceptAccount, notification);
    }
}
