package com.mindhub.todolist.servicesTests;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.NewUser;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.impl.UserServiceImpl;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mock del repositorio

    @InjectMocks
    private UserServiceImpl userService; // Inyecta los mocks en el servicio

    @Mock
    private UserEntity mockUserEntity; // Mock de UserEntity

    private NewUser mockNewUser;

    @BeforeEach
    void setUp() {
        // Simular datos del usuario
        mockUserEntity = new UserEntity("testUser@gmail.com", "TestUser", "Testpass123.");
    }

    @Test
    void getUserById() throws UserTaskNotFoundException {

        // Simular el repositorio devolviendo el usuario cuando se busca por ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUserEntity));

        // Llamamos al metodo que queremos probar
        UserEntity user = userService.getUserById(1L);

        // Verificamos que el usuario se recuperó correctamente
        assertNotNull(user);
        assertEquals("TestUser", user.getUsername());
        assertEquals("testUser@gmail.com", user.getEmail());
        assertEquals(0, user.getTasks().size());

        // Verificar que el metodo fue llamado correctamente y una vez
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByIdUserNotFound() {
        // Simular que el repositorio devolviendo el usuario vacio cuando se busca por ID
        when(userRepository.findById(mockUserEntity.getId())).thenReturn(java.util.Optional.empty());

        // Verifica que salte la excepcion
        assertThrows(UserTaskNotFoundException.class, () -> userService.getUserById(mockUserEntity.getId()));

        // Verifica que el metodo se llame una sola ves
        verify(userRepository, times(1)).findById(mockUserEntity.getId());
    }

    @Test
    void deleteUserById() throws UserTaskNotFoundException {
        // Simular que el usuario existe en la base de datos
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUserEntity));

        // Llamar al método de servicio que queremos probar
        userService.deleteUserById(1L);

        // Verificar que el metodo deleteById() fue llamado exactamente una vez
        verify(userRepository, times(1)).deleteById(1L);

        // Verificar que el usuario ya no está en la base de datos
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertTrue(userRepository.findById(1L).isEmpty());
    }

    @Test
    void deleteUserByUserNotFound() {
        // Simular que el usuario NO existe en la base de datos
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Verificar que se lanza UserTaskNotFoundException
        assertThrows(UserTaskNotFoundException.class, () -> userService.deleteUserById(1L));

        // Verificar que deleteById() no fue llamado (porque el usuario no existe)
        verify(userRepository, never()).deleteById(anyLong());
    }
}
