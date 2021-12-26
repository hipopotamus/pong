package com.hipo.dataobjcet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormErrorResult {

    private String state;

    private String exception;

    private List<ErrorDto> errorList;
}
