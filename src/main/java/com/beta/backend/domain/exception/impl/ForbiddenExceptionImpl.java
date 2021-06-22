package com.beta.backend.domain.exception.impl;

import com.beta.backend.domain.exception.AbstractCustomException;
import org.springframework.http.HttpStatus;

public class ForbiddenExceptionImpl extends AbstractCustomException {
    public ForbiddenExceptionImpl(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
    public ForbiddenExceptionImpl() {
        super("Недостаточно прав", HttpStatus.FORBIDDEN);
    }
}
