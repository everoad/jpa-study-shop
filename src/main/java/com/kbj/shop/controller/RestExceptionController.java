package com.kbj.shop.controller;

import com.kbj.shop.exception.Error;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class RestExceptionController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error> handleNotFound(ResponseStatusException ex) {
        //LOG error
        Error error = new Error();
        error.setCode(ex.getStatus().value());
        error.setMessage(ex.getReason());
        return ResponseEntity.notFound().build();
    }

}
