package com.hipo.web.dto;

import com.hipo.domain.entity.ChatRoom;
import lombok.Data;

@Data
public class ChatRoomDto {

    private Long id;

    private String name;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
    }
}
