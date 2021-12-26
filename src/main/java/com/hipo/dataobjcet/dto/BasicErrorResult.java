package com.hipo.dataobjcet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicErrorResult {

    private String state;

    private String exception;

    private String message;
}
