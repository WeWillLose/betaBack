package com.beta.backend.domain.exception.impl;

import com.beta.backend.domain.exception.AbstractCustomException;
import org.springframework.http.HttpStatus;


public class ValidationExceptionImpl extends AbstractCustomException {
    public ValidationExceptionImpl(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
