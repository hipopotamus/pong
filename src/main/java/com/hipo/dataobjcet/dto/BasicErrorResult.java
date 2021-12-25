package com.hipo.dataobjcet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicErrorResult {

    private String state;

    private String exception;

    private String message;
}
