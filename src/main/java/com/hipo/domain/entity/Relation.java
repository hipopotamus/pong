package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import com.hipo.domain.entity.enums.RelationState;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@Where(clause = "deleted = false")
public class Relation extends BaseTime {

    @Id @GeneratedValue
    @Column(name = "reration_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromAccount_id")
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toAccount_id")
    private Account toAccount;

    protected Relation() {
    }

    @Builder
    public Relation(Account fromAccount, Account toAccount, RelationState relationState) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.relationState = relationState;
    }

    @Enumerated(EnumType.STRING)
    private RelationState relationState;

    public void acceptedRequest() {
        relationState = RelationState.FRIEND;
    }

    public void block() {
        relationState = RelationState.BLOCK;
    }
}
