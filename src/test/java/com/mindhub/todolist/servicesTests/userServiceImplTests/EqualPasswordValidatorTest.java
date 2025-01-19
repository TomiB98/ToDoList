package com.mindhub.todolist.servicesTests.userServiceImplTests;

import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import com.mindhub.todolist.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EqualPasswordValidatorTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateEqualPasswordThrowsExceptionIfPasswordsAreEqual() {
        // Datos de prueba
        String currentPassword = "Current123!";
        String newPassword = "Current123!"; // Mismo que la actual

        // Mock para que passwordEncoder.matches() devuelva true cuando las contraseñas coincidan
        when(passwordEncoder.matches(newPassword, currentPassword)).thenReturn(true);

        assertThrows(BadLogInUpdateException.class, () -> {
            userService.validateEqualPassword(newPassword, currentPassword);
        });

        verify(passwordEncoder, times(1)).matches(newPassword, currentPassword);
    }

    @Test
    void testValidateEqualPasswordDoesNotThrowExceptionIfPasswordsAreDifferent() {
        // Datos de prueba
        String currentPassword = "Current123!";
        String newPassword = "NewPassword123!";

        // Mock para que passwordEncoder.matches() devuelva false cuando las contraseñas no coincidan
        when(passwordEncoder.matches(newPassword, currentPassword)).thenReturn(false);

        assertDoesNotThrow(() -> {
            userService.validateEqualPassword(newPassword, currentPassword);
        });

        verify(passwordEncoder, times(1)).matches(newPassword, currentPassword);
    }
}
