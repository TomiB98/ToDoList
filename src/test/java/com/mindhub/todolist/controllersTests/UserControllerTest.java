package com.mindhub.todolist.controllersTests;

import com.mindhub.todolist.controllers.UserController;
import com.mindhub.todolist.dtos.UpdateUser;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockSecurityContext(String email) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetUserByIdSuccess() throws Exception {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity("user@example.com", "username", "password");
        UserDTO userDTO = new UserDTO(userEntity);

        mockSecurityContext("user@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userService.getUserDTOById(userId)).thenReturn(userDTO);

        ResponseEntity<?> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testUpdateUserByIdSuccess() throws Exception {
        Long userId = 1L;
        UpdateUser updateUser = new UpdateUser("newUsername", "newPassword");
        UserDTO updatedUserDTO = new UserDTO(new UserEntity("user@example.com", "newUsername", "newPassword"));

        mockSecurityContext("user@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity("user@example.com", "username", "password")));
        when(userService.updateUserById(updateUser, userId)).thenReturn(updatedUserDTO);

        ResponseEntity<?> response = userController.updateUserById(updateUser, userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(updatedUserDTO, response.getBody());
    }

    @Test
    void testDeleteUserByIdSuccess() throws Exception {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity("user@example.com", "username", "password");

        mockSecurityContext("user@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        doNothing().when(userService).deleteUserById(userId);

        ResponseEntity<?> response = userController.deleteUserById(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetUserByIdUnauthorized() throws Exception {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity("otheruser@example.com", "username", "password");

        mockSecurityContext("user@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        ResponseEntity<?> response = userController.getUserById(userId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not authorized to access this users data", response.getBody());
    }
}

