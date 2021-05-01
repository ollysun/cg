package com.cg.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class TransactionGlobalException extends RuntimeException {

    public TransactionGlobalException(String message) {
        super(message);
    }
}
