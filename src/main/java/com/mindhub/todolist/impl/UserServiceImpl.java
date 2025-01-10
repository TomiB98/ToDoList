package com.mindhub.todolist.impl;

import com.mindhub.todolist.dtos.NewUser;
import com.mindhub.todolist.dtos.UpdateUser;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getUserDTOById(Long id) throws UserTaskNotFoundException {
        return new UserDTO(getUserById(id));
    }

    @Override
    public UserEntity getUserById(Long id) throws UserTaskNotFoundException {
        return userRepository.findById(id).orElseThrow( () -> new UserTaskNotFoundException("User not found"));
    }

    @Override
    public void createNewUser(NewUser newUser) {
        UserEntity user = new UserEntity(newUser.email(), newUser.username(), newUser.password());
        UserEntity savedUser = saveUser(user);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserDTO updateUserById(UpdateUser updatedUser, Long id) throws BadLogInUpdateException {
        UserEntity user = userRepository.findById(id).orElseThrow(()-> new BadLogInUpdateException("User not found"));
        user.setUsername(updatedUser.username());
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Override
    public void deleteUserById(Long id) throws UserTaskNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(()-> new UserTaskNotFoundException("User not found"));
        userRepository.deleteById(id);
    }
}
