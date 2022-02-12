package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
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
public class GameRoom extends BaseTime {

    @Id @GeneratedValue
    @Column(name = "gameRoom_id")
    private Long id;

    private String name;


    protected GameRoom() {
    }

    public GameRoom(String name) {
        this.name = name;
    }

}
