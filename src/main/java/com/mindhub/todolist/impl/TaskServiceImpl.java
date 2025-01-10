package com.mindhub.todolist.impl;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return  taskRepository.findById(id).orElseThrow( () -> new UserTaskNotFoundException("Task not found"));
    }

    @Override
    public TasksDTO createNewTask(NewTask newTask) {
        TaskEntity task = new TaskEntity(newTask.title(), newTask.description(), newTask.status(), newTask.user());

        TaskEntity savedTask = saveTask((task));
        return  new TasksDTO(savedTask);
    }

    @Override
    public TaskEntity saveTask(TaskEntity newTask) {
        return taskRepository.save(newTask);
    }
}
