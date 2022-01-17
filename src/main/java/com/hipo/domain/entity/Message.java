package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import com.hipo.domain.entity.enums.MessageType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@EqualsAndHashCode(of = "id")
@Where(clause = "deleted = false")
public class Message extends BaseTime {

    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    protected Message() {
    }

    public Message(String message, MessageType messageType, Account account, ChatRoom chatRoom) {
        this.message = message;
        this.messageType = messageType;
        this.account = account;
        this.chatRoom = chatRoom;
    }
}
