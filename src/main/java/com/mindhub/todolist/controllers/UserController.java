package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.NewUser;
import com.mindhub.todolist.dtos.UpdateUser;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) throws UserTaskNotFoundException {
        return userService.getUserDTOById(id);
    };

    @PostMapping("/submit")
    public ResponseEntity<?> createNewUser(@RequestBody NewUser newUser) throws BadLogInUpdateException {
        if (newUser.email() == null || newUser.email().isBlank()) {
            throw new BadLogInUpdateException("Email cannot be null or blank");
        }
        if (newUser.username() == null || newUser.username().isBlank()) {
            throw new BadLogInUpdateException("Username cannot be null or blank");
        }
        if (newUser.password() == null || newUser.password().isBlank()) {
            throw new BadLogInUpdateException("Password cannot be null or blank");
        }
        if(userRepository.findByEmail(newUser.email()) != null) {
            throw new BadLogInUpdateException("Email is already registered");
        }
        userService.createNewUser(newUser);
        return new ResponseEntity<>("User crated succesfully", HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUserById(@RequestBody UpdateUser updateUser, @PathVariable Long id) throws BadLogInUpdateException {
        if (updateUser.username() == null || updateUser.username().isBlank()) {
            throw new BadLogInUpdateException("Username cannot be null or blank");
        }
        UserDTO updatedUser = userService.updateUserById(updateUser, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteUserById(@PathVariable Long id) throws UserTaskNotFoundException {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
