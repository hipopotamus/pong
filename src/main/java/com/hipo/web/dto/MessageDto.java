package com.hipo.web.dto;

import com.hipo.domain.entity.Message;
import com.hipo.domain.entity.enums.MessageType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageDto {

    private Long id;

    private String message;

    private MessageType messageType;

    private LocalDateTime createDateTime;

    private AccountDto accountDto;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.message = message.getMessage();
        this.messageType = message.getMessageType();
        this.createDateTime = message.getCreateDate();
        this.accountDto = new AccountDto(message.getAccount());
    }
}
