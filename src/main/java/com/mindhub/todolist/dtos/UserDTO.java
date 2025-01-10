package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.UserEntity;

import java.util.List;

public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private List<TasksDTO> tasks;

    public UserDTO(UserEntity user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        tasks = user.getTasks().stream().map(task -> new TasksDTO(task)).toList();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<TasksDTO> getTasks() {
        return tasks;
    }
}
