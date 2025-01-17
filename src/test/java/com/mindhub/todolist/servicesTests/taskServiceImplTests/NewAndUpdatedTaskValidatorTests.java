package com.mindhub.todolist.servicesTests.taskServiceImplTests;

import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import org.junit.jupiter.api.Test;

import static com.mindhub.todolist.impl.TaskServiceImpl.*;
import static com.mindhub.todolist.impl.TaskServiceImpl.validateAllBlank;
import static org.junit.jupiter.api.Assertions.*;

public class NewAndUpdatedTaskValidatorTests {

    @Test
    public void testValidateNewTaskThrowsExceptionForBlankInfoInStatusTitleOrDescriptionWhenCreatingATask() {
        BadLogInUpdateException exception = assertThrows(BadLogInUpdateException.class, ()-> {
            validateMissingInfo("", "", "");
        });
        assertEquals("All fields are required.", exception.getMessage());
    }

    @Test
    public void testValidateUpdatedTaskThrowsExceptionForBlankInfoInStatusTitleAndDescription() {
        BadLogInUpdateException exception = assertThrows(BadLogInUpdateException.class, ()-> {
            validateAllBlank("", "", "");
        });
        assertEquals("At least one value must be modified.", exception.getMessage());
    }

    @Test
    public void testValidateNewAndUpdatedTaskThrowsExceptionForInvalidStatus() {
        BadLogInUpdateException exception = assertThrows(BadLogInUpdateException.class, ()-> {
            validateWrongStatus("OK");
        });
        assertEquals("Status must only be: PENDING, IN_PROGRESS, COMPLETED.", exception.getMessage());
    }

    @Test
    public void testValidateNewTaskAcceptsValidFormat() {
        assertDoesNotThrow(()-> {
            validateMissingInfo("PENDING", "TitleOfTask", "Description Of Task");
        });
    }

    @Test
    public void testValidateUpdatedTaskAcceptsValidFormat() {
        assertDoesNotThrow(()-> {
            validateAllBlank("COMPLETED", "", "");
            validateAllBlank("", "TitleOfTask", "");
            validateAllBlank("", "", "Description Of Task");
            validateAllBlank("IN_PROGRESS", "TitleOfTask", "Description Of Task");
        });
    }

    @Test
    public void testValidateStatusAcceptsValidFormat() {
        assertDoesNotThrow(()-> {
            validateWrongStatus("PENDING");
            validateWrongStatus("IN_PROGRESS");
            validateWrongStatus("COMPLETED");
        });
    }
}
