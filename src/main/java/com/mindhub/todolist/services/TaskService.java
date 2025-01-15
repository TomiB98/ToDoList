package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.dtos.UpdateTask;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    TasksDTO getTaskDTOById(Long id) throws UserTaskNotFoundException;
    TaskEntity getTaskById(Long id) throws UserTaskNotFoundException;

    List<TaskEntity> getAllTasks();

    TasksDTO createNewTask (NewTask newTask) throws UserTaskNotFoundException;
    TaskEntity saveTask(TaskEntity newTask);

    TasksDTO updateTaskById(UpdateTask updateTask, Long id) throws UserTaskNotFoundException; //throws TaskExceptions;

    void deleteTaskById(Long id) throws UserTaskNotFoundException;

    boolean isOwner(String authenticatedUserEmail, Long taskOwnerId);

    Long getTaskOwnerId(Long taskId) throws UserTaskNotFoundException;
}
