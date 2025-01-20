package com.mindhub.todolist.controllersTests;

import com.mindhub.todolist.controllers.AuthController;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.dtos.NewUser;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import com.mindhub.todolist.config.JwtUtils;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUserSuccess() {
        LoginUser loginUser = new LoginUser("user@example.com", "Password123!");

        // Mock authentication response
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("user@example.com"); // Ensure it returns the expected value
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Mock JWT generation
        when(jwtUtils.generateToken("user@example.com")).thenReturn("mocked-jwt-token");

        // Call the method
        ResponseEntity<String> response = authController.authenticateUser(loginUser);

        // Assertions
        assertNotNull(response.getBody(), "JWT Token should not be null");
        assertEquals("mocked-jwt-token", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAuthenticateUserInvalidCredentials() {
        LoginUser loginUser = new LoginUser("user@example.com", "WrongPassword");

        // Mocking exception during authentication
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));

        // Calling the method
        ResponseEntity<String> response = authController.authenticateUser(loginUser);

        // Assertions
        assert(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
        assert(response.getBody().equals("Password or email invalid."));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        NewUser newUser = new NewUser("user@example.com", "username", "Password123.", "USER");

        // Mocking user creation
        doNothing().when(userService).createNewUser(any());

        // Calling the method
        ResponseEntity<?> response = authController.register(newUser);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created.", response.getBody());
    }

    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        NewUser newUser = new NewUser("user@example.com", "username", "Password123.", "USER");

        // Mocking user existence check
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new UserEntity()));

        // Calling the method
        ResponseEntity<?> response = authController.register(newUser);

        // Assertions
        assert(response.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert(response.getBody().equals("This email is already registered."));
    }

    @Test
    void testRegisterInvalidEmail() {
        NewUser newUser = new NewUser("userexample.com", "username", "Password123.", "ADMIN");

        // Calling the method
        ResponseEntity<?> response = authController.register(newUser);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid email format: must have at least 8 characters and a '@'.", response.getBody());
    }

    @Test
    void testRegisterInvalidRole() {
        NewUser newUser = new NewUser("user@example.com", "username", "Password123.", "INVALID_ROLE");

        // Calling the method
        ResponseEntity<?> response = authController.register(newUser);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Role must be ADMIN or USER.", response.getBody());
    }
}
