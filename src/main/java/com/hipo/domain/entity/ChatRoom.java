package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseBy;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Where(clause = "deleted = false")
public class ChatRoom extends BaseBy {

    @Id
    @GeneratedValue
    @Column(name = "chatRoom_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "masterAccount_id")
    private Account masterAccount;

    @OneToMany(mappedBy = "chatRoom")
    private List<AccountChatRoom> participants = new ArrayList<>();

    protected ChatRoom() {
    }

    public ChatRoom(String name, Account masterAccount) {
        this.name = name;
        this.masterAccount = masterAccount;
    }

    public void updateChatRoomName(String updateChatRoomName) {
        this.name = updateChatRoomName;
    }

    public void updateChatRoomMaster(Account updateMasterAccount) {
        this.masterAccount = updateMasterAccount;
    }
}
