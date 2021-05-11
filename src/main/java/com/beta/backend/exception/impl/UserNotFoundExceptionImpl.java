package com.beta.backend.exception.impl;

import com.beta.backend.exception.AbstractCustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundExceptionImpl extends AbstractCustomException {


    public UserNotFoundExceptionImpl(String username) {
        super(String.format("Пользователь с username: %s не найден",username),HttpStatus.NOT_FOUND);
    }

    public UserNotFoundExceptionImpl(Long id) {
        super(String.format("Пользователь с id: %s не найден",id),HttpStatus.NOT_FOUND);
    }

    public UserNotFoundExceptionImpl() {
        super("Пользователь  не найден",HttpStatus.NOT_FOUND);
    }
}
