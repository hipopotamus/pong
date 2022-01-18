package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import com.hipo.domain.entity.enums.NotificationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@EqualsAndHashCode(of = "id")
@Where(clause = "deleted = false")
public class Notification extends BaseTime {

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private String message;

    private boolean checked;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    protected Notification() {
    }

    public Notification(String message, NotificationType notificationType, Account account) {
        this.message = message;
        this.notificationType = notificationType;
        this.account = account;
        this.checked = false;
    }

    public void check() {
        this.checked = true;
    }
}
