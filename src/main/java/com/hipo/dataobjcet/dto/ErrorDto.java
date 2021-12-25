package com.hipo.dataobjcet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDto {

    private String field;

    private String code;

    private String message;

    public ErrorDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
