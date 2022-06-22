package com.hipo.web.dto;

import com.hipo.domain.entity.ChatRoom;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChatRoomDto {

    private Long id;

    private String name;

    private int size;

    private List<AccountDto> participant;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
        this.size = chatRoom.getParticipants().size();
        this.participant = chatRoom.getParticipants().stream()
                .map(accountChatRoom -> new AccountDto(accountChatRoom.getAccount()))
                .collect(Collectors.toList());
    }
}
