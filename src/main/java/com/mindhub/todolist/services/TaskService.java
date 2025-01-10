package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {

    TasksDTO getTaskDTOById(Long id) throws UserTaskNotFoundException;
    TaskEntity getTaskById(Long id) throws UserTaskNotFoundException;

//    List<TaskEntity> getTasks();
//
    TasksDTO createNewTask (NewTask newTask);
    TaskEntity saveTask(TaskEntity newTask);
//
//    TasksDTO updateTaskById(UpdateTask updateTask, Long id); //throws TaskExceptions;
//
//    void deleteTaskById(Long id);
}
