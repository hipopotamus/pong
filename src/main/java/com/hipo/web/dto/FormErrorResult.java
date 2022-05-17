package com.hipo.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class FormErrorResult {

    private int state;

    private String exception;

    private ArrayList<FieldErrorDto> fieldErrorList;

    private ArrayList<GlobalErrorDto> globalErrorList;

    @Data
    @AllArgsConstructor
    static public class FieldErrorDto {

        private String field;

        private String code;

        private String message;
    }

    @Data
    @AllArgsConstructor
    static public class GlobalErrorDto {

        private String code;

        private String message;
    }
}
