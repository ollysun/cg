package com.cg.test.exception;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class ExceptionResponse {

    private LocalDateTime timestamp;
    private String message;
    private HttpStatus status;
    private List<String> errors;

    public ExceptionResponse(){
        super();
    }

    public ExceptionResponse(final HttpStatus status,final LocalDateTime timestamp, final String message, final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public ExceptionResponse(final HttpStatus status, final LocalDateTime timestamp, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
        this.timestamp = timestamp;
    }
}
