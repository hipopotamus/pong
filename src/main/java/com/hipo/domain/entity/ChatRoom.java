package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseBy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@EqualsAndHashCode(of = "id")
@Where(clause = "deleted = false")
public class ChatRoom extends BaseBy {

    @Id
    @GeneratedValue
    @Column(name = "chatRoom_id")
    private Long id;

    private String name;

    protected ChatRoom() {
    }

    public ChatRoom(String name) {
        this.name = name;
    }
}
