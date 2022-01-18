package com.hipo.listener.event;

import com.hipo.domain.entity.Relation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestEvent {

    private Relation relation;
}
