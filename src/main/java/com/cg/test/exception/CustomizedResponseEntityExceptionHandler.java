package com.cg.test.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler  {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                          HttpHeaders headers,
                                                          HttpStatus status,
                                                          WebRequest request) {

        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,LocalDateTime.now(), null, errors);
        return handleExceptionInternal(ex, exceptionResponse, headers, exceptionResponse.getStatus(), request);

    }

    @ExceptionHandler({TransactionGlobalException.class})
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                LocalDateTime.now(),
                ex.getMessage(),
                "error occurred");
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }


}