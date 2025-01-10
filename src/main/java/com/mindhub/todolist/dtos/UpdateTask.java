package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.TaskStatus;

public record UpdateTask(String title, String description, TaskStatus status) {
}
