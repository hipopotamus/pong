package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountChatRoom extends BaseTime {

    @Id
    @GeneratedValue
    @Column(name = "accountChatRoom_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    public AccountChatRoom(Account account, ChatRoom chatRoom) {
        this.account = account;
        this.chatRoom = chatRoom;
        chatRoom.getParticipants().add(this);
    }
}
