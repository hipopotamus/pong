package com.hipo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicErrorResult {

    private int state;

    private String exception;

    private String message;
}
