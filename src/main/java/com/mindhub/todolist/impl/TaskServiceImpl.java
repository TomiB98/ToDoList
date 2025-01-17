package com.mindhub.todolist.impl;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.dtos.UpdateTask;
import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TasksDTO getTaskDTOById(Long id) throws UserTaskNotFoundException {
        return new TasksDTO(getTaskById(id));
    }

    @Override
    public TaskEntity getTaskById(Long id) throws UserTaskNotFoundException {
        return  taskRepository.findById(id).orElseThrow( () -> new UserTaskNotFoundException("Task with ID " + id + " not found."));
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        return  taskRepository.findAll();
    }

    @Override
    public TasksDTO createNewTask(NewTask newTask) throws Exception {
        validateNewTask(newTask);
        TaskStatus status = TaskStatus.valueOf(newTask.status());
        TaskEntity task = new TaskEntity(newTask.title(), newTask.description(), status, newTask.user());
        TaskEntity savedTask = saveTask((task));
        return  new TasksDTO(savedTask);
    }

    @Override
    public TaskEntity saveTask(TaskEntity newTask) {
        return taskRepository.save(newTask);
    }

    @Override
    public TasksDTO updateTaskById(UpdateTask updateTask, Long id) throws Exception {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new UserTaskNotFoundException("Task with ID " + id + " not found."));

        validateUpdatedTask(updateTask);

        if (!updateTask.title().isBlank()) {
            task.setTitle(updateTask.title());
        }

        if (!updateTask.description().isBlank()) {
            task.setDescription(updateTask.description());
        }

        if (updateTask.status().isBlank()) {
            TaskStatus status = TaskStatus.valueOf(updateTask.status());
            task.setStatus(status);
        }

        TaskEntity updatedTask = taskRepository.save(task);
        return new TasksDTO(updatedTask);
    }

    @Override
    public void deleteTaskById(Long id) throws UserTaskNotFoundException {
        TaskEntity task = taskRepository.findById(id).orElseThrow(()-> new UserTaskNotFoundException("Task with ID " + id + " not found."));
        taskRepository.deleteById(id);
    }

    public Long getTaskOwnerId(Long taskId) throws UserTaskNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new UserTaskNotFoundException("Task not found"))
                .getUser()
                .getId();
    }

    public boolean isOwner(String authenticatedUserEmail, Long taskOwnerId) {
        return userRepository.findByEmail(authenticatedUserEmail)
                .map(user -> user.getId().equals(taskOwnerId))
                .orElse(false);
    }

    // Validations //

    public void validateNewTask (NewTask newTask) throws Exception {
        validateMissingInfo(newTask.status(), newTask.title(), newTask.description());
        validateWrongStatus(newTask.status());
    }

    public static void validateMissingInfo(String status, String title, String description) throws BadLogInUpdateException {
        if(status.isBlank() || title.isBlank() || description.isBlank()) {
            throw new BadLogInUpdateException("All fields are required.");
        }
    }

    public void validateUpdatedTask(UpdateTask updateTask) throws Exception {
        validateAllBlank(updateTask.status(), updateTask.title(), updateTask.description());
        validateWrongStatus(updateTask.status());
    }

    public static void validateAllBlank(String status, String title, String description) throws BadLogInUpdateException {
        if(status.isBlank() && title.isBlank() && description.isBlank()) {
            throw new BadLogInUpdateException("At least one value must be modified.");
        }
    }

    public static void validateWrongStatus(String status) throws BadLogInUpdateException {
        if(!status.equals("PENDING") && !status.equals("IN_PROGRESS") && !status.equals("COMPLETED")) {
            throw new BadLogInUpdateException("Status must only be: PENDING, IN_PROGRESS, COMPLETED.");
        }
    }
}



//        if (updateTask.title().isBlank()) {
//            task.setTitle(task.getTitle());
//        } else task.setTitle(updateTask.title());
//
//        if (updateTask.description().isBlank()) {
//            task.setDescription(task.getDescription());
//        } else task.setDescription(updateTask.description());
//
//        if (updateTask.status() == null) {
//            task.setStatus(task.getStatus());
//        } else task.setStatus(updateTask.status());
