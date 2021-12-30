package com.hipo.controller.advice;

import com.hipo.dataobjcet.dto.BasicErrorResult;
import com.hipo.dataobjcet.dto.ErrorDto;
import com.hipo.dataobjcet.dto.FormErrorResult;
import com.hipo.exception.IllegalFormException;
import com.hipo.exception.NonExistResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice(annotations = RestController.class)
@Component
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NonExistResourceException.class})
    public BasicErrorResult NonExistResourceExceptionHandler(NonExistResourceException exception) {
        return new BasicErrorResult("400", exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public BasicErrorResult HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
        return new BasicErrorResult("400", exception.getClass().getSimpleName(), "잘못된 형식의 요청입니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalFormException.class})
    public FormErrorResult IllegalFormExceptionHandler(IllegalFormException exception) {
        List<ErrorDto> errorList = getFieldErrorList(exception.getErrors().getAllErrors());

        return new FormErrorResult("400", exception.getClass().getSimpleName(), errorList);
    }

    private List<ErrorDto> getFieldErrorList(List<ObjectError> errors) {
        List<ErrorDto> errorList = new ArrayList<>();
        for (ObjectError error : errors) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errorList.add(new ErrorDto(fieldError.getField(), fieldError.getCode(), getErrorMessage(error)));
            } else {
                errorList.add(new ErrorDto(error.getCode(), getErrorMessage(error)));
            }
        }

        return errorList;
    }

    private String getErrorMessage(ObjectError error) {
        String[] codes = error.getCodes();
        for (String code : codes) {
            try {
                return messageSource.getMessage(code, null, Locale.KOREA);
            } catch (NoSuchMessageException ignored) {}
        }

        return error.getDefaultMessage();
    }
}
