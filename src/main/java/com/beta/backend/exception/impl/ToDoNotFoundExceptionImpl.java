package com.beta.backend.exception.impl;

import com.beta.backend.exception.AbstractCustomException;
import org.springframework.http.HttpStatus;

public class ToDoNotFoundExceptionImpl extends AbstractCustomException {

    public ToDoNotFoundExceptionImpl(Long id) {
        super(String.format("Заметка с id: %s не найдена",id),HttpStatus.NOT_FOUND);
    }

    public ToDoNotFoundExceptionImpl() {
        super("Заметка не найдена",HttpStatus.NOT_FOUND);
    }
}
