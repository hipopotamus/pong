package com.hipo.dataobjcet.dto;

import lombok.Data;

@Data
public class MessageResult {
    private final String message;

    public MessageResult(String message) {
        this.message = message;
    }
}
