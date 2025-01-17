package com.mindhub.todolist.services;

import com.mindhub.todolist.dtos.NewUser;
import com.mindhub.todolist.dtos.UpdateUser;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.UserEntity;

import java.util.List;

public interface UserService {

    UserDTO getUserDTOById (Long id) throws UserTaskNotFoundException;
    UserEntity getUserById (Long id) throws UserTaskNotFoundException;

    void createNewUser(NewUser newUser);
    UserEntity saveUser(UserEntity user);

    UserDTO updateUserById(UpdateUser updatedUser, Long id) throws Exception;

    void deleteUserById(Long id) throws UserTaskNotFoundException;

    List<UserDTO> getAllUsers();
}
