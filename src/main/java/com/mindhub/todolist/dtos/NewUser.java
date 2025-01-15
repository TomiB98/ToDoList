package com.mindhub.todolist.dtos;

import com.mindhub.todolist.models.RoleType;

public record NewUser(String email, String username, String password, RoleType role) {
}
