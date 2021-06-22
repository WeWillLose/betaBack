package com.beta.backend.domain.exception.impl;

import com.beta.backend.domain.exception.AbstractCustomException;
import org.springframework.http.HttpStatus;

public class JwtExceptionImpl  extends AbstractCustomException {

    public JwtExceptionImpl(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
