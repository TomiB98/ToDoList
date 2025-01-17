package com.mindhub.todolist;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;

import static com.mindhub.todolist.controllers.AuthController.validatePassword;
import static com.mindhub.todolist.impl.UserServiceImpl.validateUpdatedPassword;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    @Test
    public void testValidatePasswordThrowsExceptionForBlankPassword() {
        ValidationException exception = assertThrows(ValidationException.class, ()-> {
            validatePassword("");
        });
        assertEquals("Password cannot be null or blank.", exception.getMessage());
    }

    @Test
    public void testValidatePasswordThrowsExceptionForInvalidPasswordAuthControllerAndUserService() {
        ValidationException exception = assertThrows(ValidationException.class, ()-> {
            validatePassword("short!");
            validateUpdatedPassword("short!");
        });
        assertEquals("Password must have at lest: one digit, a lower and upper case letter, a special character, 8 characters and no whitespace.", exception.getMessage());
    }

    @Test
    public void testValidatePasswordAcceptsValidFormatAuthControllerAndUserService() {
        assertDoesNotThrow(()-> {
            validatePassword("Correct123.");
            validateUpdatedPassword("Correct123.");
        });
    }
}
