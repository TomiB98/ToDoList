package com.mindhub.todolist.controllersTests;

import com.mindhub.todolist.controllers.TaskController;
import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.dtos.UpdateTask;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetTaskById_Success() throws UserTaskNotFoundException {
        Long taskId = 1L;
        TasksDTO taskDTO = new TasksDTO();
        when(taskService.getTaskDTOById(taskId)).thenReturn(taskDTO);

        ResponseEntity<?> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDTO, response.getBody());
    }

    @Test
    void testGetAllTasks_Success() throws UserTaskNotFoundException {
        // Mock data
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle("Test Title");
        taskEntity.setDescription("Test Description");
        taskEntity.setStatus(TaskStatus.PENDING);
        List<TaskEntity> taskEntities = List.of(taskEntity);

        when(taskService.getAllTasks()).thenReturn(taskEntities);

        // Convert entities to DTOs manually in the test
        List<TasksDTO> expectedTasks = taskEntities.stream().map(TasksDTO::new).toList();

        // Call the controller method
        ResponseEntity<?> response = taskController.getAllTasks();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTasks, response.getBody());
    }

    @Test
    void testCreateTask_Success() throws Exception {
        NewTask newTask = new NewTask("title", "description", "PENDING", 1L);
        UserEntity userEntity = new UserEntity();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(taskService.createNewTask(any())).thenReturn(new TasksDTO());

        ResponseEntity<?> response = taskController.createTask(newTask);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateTaskById_Success() throws Exception {
        UpdateTask updateTask = new UpdateTask("new title", "new description", "COMPLETED");
        Long taskId = 1L;
        when(taskService.updateTaskById(any(), anyLong())).thenReturn(new TasksDTO());

        ResponseEntity<?> response = taskController.updateTaskById(updateTask, taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteTaskById_Success() throws UserTaskNotFoundException {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTaskById(taskId);

        ResponseEntity<?> response = taskController.deleteTaskById(taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

