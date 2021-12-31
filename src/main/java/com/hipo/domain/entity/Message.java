package com.hipo.domain.entity;

import com.hipo.domain.entity.enums.MessageType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@EqualsAndHashCode(of = "id")
@Where(clause = "deleted = false")
public class Message {

    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String message;

    @ManyToOne
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

}
