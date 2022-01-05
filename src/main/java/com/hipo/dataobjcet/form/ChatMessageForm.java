package com.hipo.dataobjcet.form;

import com.hipo.domain.entity.enums.MessageType;
import lombok.Data;

@Data
public class ChatMessageForm {

    private Long roomId;

    private String writer;

    private String message;

    private MessageType messageType;
}
