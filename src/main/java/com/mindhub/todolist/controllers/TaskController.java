package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.dtos.UpdateTask;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    private ResponseEntity<?> getTaskById(@PathVariable Long id) throws UserTaskNotFoundException{
        TasksDTO tasksDTO = taskService.getTaskDTOById(id);
        return new ResponseEntity<> (tasksDTO, HttpStatus.OK);
    }

    @PostMapping("/submit")
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
    public ResponseEntity<?> updateTaskById(@RequestBody UpdateTask updateTask, @PathVariable Long id) throws UserTaskNotFoundException {
        TasksDTO updatedTask = taskService.updateTaskById(updateTask, id);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

//    @DeleteMapping
}
