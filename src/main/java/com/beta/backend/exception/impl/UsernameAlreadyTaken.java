package com.beta.backend.exception.impl;

import com.beta.backend.exception.AbstractCustomException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyTaken extends AbstractCustomException {
    private static final long serialVersionUID = 1L;

    public UsernameAlreadyTaken(String username) {
        super(String.format("Пользователь с username: %s уже существует",username), HttpStatus.CONFLICT);
    }
}
