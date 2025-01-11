package com.mindhub.todolist.impl;

import com.mindhub.todolist.dtos.NewTask;
import com.mindhub.todolist.dtos.TasksDTO;
import com.mindhub.todolist.dtos.UpdateTask;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
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
    public TasksDTO createNewTask(NewTask newTask) throws UserTaskNotFoundException {
        TaskEntity task = new TaskEntity(newTask.title(), newTask.description(), newTask.status(), newTask.user());
        TaskEntity savedTask = saveTask((task));
        return  new TasksDTO(savedTask);
    }

    @Override
    public TaskEntity saveTask(TaskEntity newTask) {
        return taskRepository.save(newTask);
    }

    @Override
    public TasksDTO updateTaskById(UpdateTask updateTask, Long id) throws UserTaskNotFoundException {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new UserTaskNotFoundException("Task with ID " + id + " not found."));
        if (updateTask.title().isBlank()) {
            task.setTitle(task.getTitle());
        } else task.setTitle(updateTask.title());

        if (updateTask.description().isBlank()) {
            task.setDescription(task.getDescription());
        } else task.setDescription(updateTask.description());

        if (updateTask.status() == null) {
            task.setStatus(task.getStatus());
        } else task.setStatus(updateTask.status());

        TaskEntity updatedTask = taskRepository.save(task);
        return new TasksDTO(updatedTask);
    }

    @Override
    public void deleteTaskById(Long id) throws UserTaskNotFoundException {
        TaskEntity task = taskRepository.findById(id).orElseThrow(()-> new UserTaskNotFoundException("Task with ID " + id + " not found."));
        taskRepository.deleteById(id);
    }
}
