package com.mindhub.todolist.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(UserTaskNotFoundException.class)
    public ResponseEntity<String> userExceptionHandler(UserTaskNotFoundException userException){
        return new ResponseEntity<>(userException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadLogInUpdateException.class)
    public ResponseEntity<String> userExceptionHandler(BadLogInUpdateException duplicateEmailException){
        return new ResponseEntity<>(duplicateEmailException.getMessage(), HttpStatus.CONFLICT);
    }
}
