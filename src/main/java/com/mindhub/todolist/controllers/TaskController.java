package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.dtos.UpdateTask;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<String> noIdProvided() {
        return ResponseEntity.badRequest().body("The provided ID cannot be null or empty.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a task data with the id", description = "Receives an id and returns all the data of the specified tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid id.")
    })
    private ResponseEntity<?> getTaskById(@PathVariable Long id) throws UserTaskNotFoundException{
        TasksDTO tasksDTO = taskService.getTaskDTOById(id);
        return new ResponseEntity<> (tasksDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Gets all the tasks", description = "Returns all the tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, pool task empty.")
    })
    private ResponseEntity<?> getAllTasks() throws UserTaskNotFoundException {
        List<TasksDTO> tasklist = taskService.getAllTasks()
                .stream()
                .map(TasksDTO::new)
                .toList();
        if(tasklist.size() == 0) {
             throw new UserTaskNotFoundException("There are no tasks available");
        }
        return new ResponseEntity<>(tasklist, HttpStatus.OK);
    }

    @PostMapping("/submit")
    @Operation(summary = "Creates a new task", description = "Receives a title, description, status and user id, creates a new task for thr specified user and returns the task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created."),
            @ApiResponse(responseCode = "409", description = "Bad request, invalid data.")
    })
    public ResponseEntity<?> createTask(@RequestBody NewTask newTask) throws UserTaskNotFoundException {
        if(newTask.title() == null || newTask.title().isBlank()) {
            return new ResponseEntity<>("Title cannot be null or blank", HttpStatus.BAD_REQUEST);
        }
        if(newTask.description() == null || newTask.description().isBlank()) {
            return new ResponseEntity<>("Description cannot be null or blank", HttpStatus.BAD_REQUEST);
        }
        if(newTask.status() == null) {
            return new ResponseEntity<>("Status cannot be null or blank", HttpStatus.BAD_REQUEST);
        }
        if(newTask.user() == null) {
            return new ResponseEntity<>("User cannot be null or blank", HttpStatus.BAD_REQUEST);
        }
        boolean userExists = userRepository.existsById(newTask.user().getId());
        if (!userExists) {
            throw new UserTaskNotFoundException("User not found");
        }
        TasksDTO savedTask = taskService.createNewTask(newTask);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Updates a task", description = "Receives an id and updates the assigned task, you can update title, description or status independently if you leave one blank it will retrieve the old value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully updated."),
            @ApiResponse(responseCode = "409", description = "Bad request, task not found.")
    })
    public ResponseEntity<?> updateTaskById(@RequestBody UpdateTask updateTask, @PathVariable Long id) throws UserTaskNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        Long taskOwnerId = taskService.getTaskOwnerId(id);

        if (!isAdmin && !taskService.isOwner(authenticatedUserEmail, taskOwnerId)) {
            return new ResponseEntity<>("Unauthorized to update this task", HttpStatus.FORBIDDEN);
        }

        TasksDTO updatedTask = taskService.updateTaskById(updateTask, id);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a task", description = "Receives an id and and deletes the assigned task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted."),
            @ApiResponse(responseCode = "400", description = "Bad request, task not found.")
    })
    public ResponseEntity<?> deleteTaskById(@PathVariable Long id) throws UserTaskNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        Long taskOwnerId = taskService.getTaskOwnerId(id);

        if (!isAdmin && !taskService.isOwner(authenticatedUserEmail, taskOwnerId)) {
            return new ResponseEntity<>("Unauthorized to delete this task", HttpStatus.FORBIDDEN);
        }

        taskService.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
