package com.mindhub.todolist.exeptions;

public class UserTaskNotFoundException extends Exception {

    public UserTaskNotFoundException(String message) {
        super(message);
    }
}
