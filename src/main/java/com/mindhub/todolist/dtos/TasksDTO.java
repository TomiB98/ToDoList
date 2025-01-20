package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.TaskStatus;

public class TasksDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;

    public TasksDTO() {}

    public TasksDTO(TaskEntity task) {
        id = task.getId();
        title = task.getTitle();
        description = task.getDescription();
        status = task.getStatus();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
