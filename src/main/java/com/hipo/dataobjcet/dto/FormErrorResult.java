package com.hipo.dataobjcet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FormErrorResult {

    private String state;

    private String exception;

    private List<ErrorDto> fieldErrorList;
}
