package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserEntity;

public record NewTask(String title, String description, TaskStatus status, UserEntity user) {
}
