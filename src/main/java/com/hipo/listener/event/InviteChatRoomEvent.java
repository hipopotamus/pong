package com.hipo.listener.event;

import com.hipo.domain.entity.Account;
import com.hipo.domain.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteChatRoomEvent {

    private Account inviteAccount;

    private Account acceptAccount;

    private ChatRoom chatRoom;

}
