package com.hipo.domain.entity;

import com.hipo.domain.entity.base.BaseTime;
import com.hipo.domain.entity.enums.RelationState;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Where(clause = "deleted = false")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relation extends BaseTime {

    @Id @GeneratedValue
    @Column(name = "relation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromAccount_id")
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toAccount_id")
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    private RelationState relationState;

    public void acceptRequest() {
        relationState = RelationState.FRIEND;
    }

    public void block() {
        relationState = RelationState.BLOCK;
    }
}
