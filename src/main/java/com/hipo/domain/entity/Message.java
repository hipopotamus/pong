package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import com.hipo.domain.entity.enums.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Message(String message, MessageType messageType, Account account, ChatRoom chatRoom) {
        this.message = message;
        this.messageType = messageType;
        this.account = account;
        this.chatRoom = chatRoom;
    }

    public void deleteMessage() {
        this.message = "삭제된 메시지 입니다.";
        this.messageType = MessageType.DELETE;
    }
}
