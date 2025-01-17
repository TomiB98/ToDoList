package com.mindhub.todolist.servicesTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UserEntity mockUser;
    private NewTask mockNewTask;
    private TaskEntity mockTaskEntity;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();

        mockNewTask = new NewTask("Test Task", "This is a test task", "PENDING", mockUser);
        mockTaskEntity = new TaskEntity("Test Task", "This is a test task", TaskStatus.PENDING, mockUser);
    }

    @Test
    void testCreateNewTaskSuccess() throws Exception {
        // Simular comportamiento del repositorio
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(mockTaskEntity);

        // Llamar al servicio
        TasksDTO result = taskService.createNewTask(mockNewTask);

        // Verificar que el repositorio fue llamado con los parámetros correctos
        verify(taskRepository, times(1)).save(any(TaskEntity.class));

        // Asegurar que los valores de retorno sean los esperados
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("This is a test task", result.getDescription());
        assertEquals("PENDING", result.getStatus().toString());
    }

    @Test
    void testCreateNewTaskMissingInfo() {
        NewTask invalidTask = new NewTask("", "", "", mockUser);

        Exception exception = assertThrows(BadLogInUpdateException.class, () -> {
            taskService.createNewTask(invalidTask);
        });

        assertEquals("All fields are required.", exception.getMessage());
    }


    @Test
    void testGetTaskById() throws UserTaskNotFoundException {

        when(taskRepository.findById(mockTaskEntity.getId())).thenReturn(java.util.Optional.of(mockTaskEntity));


        TaskEntity task = taskService.getTaskById(mockTaskEntity.getId());


        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        assertEquals("This is a test task", task.getDescription());
        assertEquals(TaskStatus.PENDING, task.getStatus());


        verify(taskRepository, times(1)).findById(mockTaskEntity.getId());
    }

    @Test
    void testGetTaskByIdTaskNotFound() {

        when(taskRepository.findById(mockTaskEntity.getId())).thenReturn(java.util.Optional.empty());

        assertThrows(UserTaskNotFoundException.class, () -> taskService.getTaskById(mockTaskEntity.getId()));


        verify(taskRepository, times(1)).findById(mockTaskEntity.getId());
    }

    @Test
    void testGetTaskDTOById() throws UserTaskNotFoundException {

        when(taskRepository.findById(mockTaskEntity.getId())).thenReturn(java.util.Optional.of(mockTaskEntity));


        TasksDTO taskDTO = taskService.getTaskDTOById(mockTaskEntity.getId());


        assertNotNull(taskDTO);
        assertEquals("Test Task", taskDTO.getTitle());
        assertEquals("This is a test task", taskDTO.getDescription());
        assertEquals(TaskStatus.PENDING, taskDTO.getStatus());


        verify(taskRepository, times(1)).findById(mockTaskEntity.getId());
    }

    @Test
    void testGetTaskDTOByIdTaskNotFound() {

        when(taskRepository.findById(mockTaskEntity.getId())).thenReturn(java.util.Optional.empty());


        assertThrows(UserTaskNotFoundException.class, () -> taskService.getTaskDTOById(mockTaskEntity.getId()));


        verify(taskRepository, times(1)).findById(mockTaskEntity.getId());
    }
}
