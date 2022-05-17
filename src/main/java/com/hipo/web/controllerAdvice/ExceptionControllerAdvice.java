package com.hipo.web.controllerAdvice;

import com.hipo.exception.DuplicationRequestException;
import com.hipo.exception.IllegalRequestException;
import com.hipo.exception.NonExistResourceException;
import com.hipo.web.dto.BasicErrorResult;
import com.hipo.web.dto.FormErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.hipo.web.dto.FormErrorResult.FieldErrorDto;
import static com.hipo.web.dto.FormErrorResult.GlobalErrorDto;

@RestControllerAdvice(annotations = RestController.class)
@Component
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public BasicErrorResult NonExistResourceExceptionHandler(NonExistResourceException exception) {
        return new BasicErrorResult(HttpStatus.NOT_FOUND.value(), exception.getClass().getSimpleName(),
                exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalRequestException.class})
    public BasicErrorResult IllegalRequestExceptionHandler(IllegalRequestException exception) {
        return new BasicErrorResult(HttpStatus.BAD_REQUEST.value(), exception.getClass().getSimpleName(),
                exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DuplicationRequestException.class})
    public BasicErrorResult DuplicationRequestException(DuplicationRequestException exception) {
        return new BasicErrorResult(HttpStatus.BAD_REQUEST.value(), exception.getClass().getSimpleName(),
                exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public FormErrorResult BindExceptionHandler(BindException exception) {

        List<FieldError> fieldErrors = exception.getFieldErrors();
        List<ObjectError> globalErrors = exception.getGlobalErrors();

        return new FormErrorResult(HttpStatus.BAD_REQUEST.value(), exception.getClass().getSimpleName(),
                getFieldErrorList(fieldErrors), getGlobalErrorList(globalErrors));
    }

    private ArrayList<FieldErrorDto> getFieldErrorList(List<FieldError> fieldErrors) {
        if (fieldErrors == null) {
            return new ArrayList<>();
        }

        return fieldErrors.stream()
                .map(error -> new FieldErrorDto(error.getField(), error.getCode(), getErrorMessage(error)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<GlobalErrorDto> getGlobalErrorList(List<ObjectError> globalErrors) {
        if (globalErrors == null) {
            return new ArrayList<>();
        }

        return globalErrors.stream()
                .map(error -> new GlobalErrorDto(error.getCode(), getErrorMessage(error)))
                .collect(Collectors.toCollection(ArrayList::new));
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
