package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@EqualsAndHashCode(of = "id")
@Where(clause = "deleted = false")
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

    protected AccountChatRoom() {
    }

    public AccountChatRoom(Account account, ChatRoom chatRoom) {
        this.account = account;
        this.chatRoom = chatRoom;
    }
}
