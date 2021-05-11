package com.beta.backend.exception.handler;

import com.beta.backend.dto.ErrorResponseDTO;
import com.beta.backend.exception.AbstractCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> validExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream().map(t -> t.getField() + ", " + t.getDefaultMessage()).collect(Collectors.toList());
        return new ErrorResponseDTO(collect,HttpStatus.BAD_REQUEST).getResponseEntity();

    }

    @ExceptionHandler(AbstractCustomException.class)
    public ResponseEntity<Object> customExceptionHandler(AbstractCustomException e) {
        return new ErrorResponseDTO(e.getMessage(),e.getHttpStatus()).getResponseEntity();

    }
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(HttpRequestMethodNotSupportedException e) {
        return new ErrorResponseDTO("Данный тип запроса не поддерживается",HttpStatus.BAD_REQUEST).getResponseEntity();
    }
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(HttpMessageNotReadableException e) {
        return new ErrorResponseDTO("Ошибка парсинга данных",HttpStatus.BAD_REQUEST).getResponseEntity();
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleConstraintViolationException(AccessDeniedException e) {
        return new ErrorResponseDTO(e.getMessage(),HttpStatus.FORBIDDEN).getResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> ServerError(Exception e) {
        log.error("Unhandled Error",e);
        return new ErrorResponseDTO("Ошибка сервера",HttpStatus.INTERNAL_SERVER_ERROR).getResponseEntity();
    }








}
