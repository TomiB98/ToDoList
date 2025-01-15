package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.UpdateUser;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<String> noIdProvided() {
        return ResponseEntity.badRequest().body("The provided ID cannot be null or empty.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets the user data with the id", description = "Receives an id and returns all the data of the specified user + all his tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "403", description = "Forbidden access to another users data."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid id.")
    })
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws UserTaskNotFoundException {
        // Retrives the authenticated user
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // Finds the user by Id
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        // Verify if the email of the user matches the one authenticated
        if (!userEntity.getEmail().equals(authenticatedEmail)) {
            return new ResponseEntity<>("You are not authorized to access this users data", HttpStatus.FORBIDDEN);
        }
        // Returns the user data
        UserDTO userDTO = userService.getUserDTOById(id);
        return ResponseEntity.ok(userDTO);
    }
//    public UserDTO getUserById(@PathVariable Long id) throws UserTaskNotFoundException {
//        return userService.getUserDTOById(id);
//    };


    @PutMapping("update/{id}")
    @Operation(summary = "Updates a user", description = "Receives an id and updates the assigned user, you can update username or password independently, if you leave one blank it will retrieve the old value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully updated."),
            @ApiResponse(responseCode = "403", description = "Forbidden access to another users data."),
            @ApiResponse(responseCode = "409", description = "Bad request, user not found.")
    })
    public ResponseEntity<?> updateUserById(@RequestBody UpdateUser updateUser, @PathVariable Long id) throws BadLogInUpdateException {
        // Retrives the authenticated user
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // Finds the user by Id
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        // Verify if the email of the user matches the one authenticated
        if (!userEntity.getEmail().equals(authenticatedEmail)) {
            return new ResponseEntity<>("You are not authorized to update this users data", HttpStatus.FORBIDDEN);
        }
        // Updates User
        UserDTO updatedUser = userService.updateUserById(updateUser, id);
        return ResponseEntity.ok(updatedUser);
    }
//    public ResponseEntity<?> updateUserById(@RequestBody UpdateUser updateUser, @PathVariable Long id) throws BadLogInUpdateException {
//        UserDTO updatedUser = userService.updateUserById(updateUser, id);
//        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Receives an id and and deletes the assigned user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted."),
            @ApiResponse(responseCode = "403", description = "Forbidden access to another users data."),
            @ApiResponse(responseCode = "400", description = "Bad request, user not found.")
    })
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) throws UserTaskNotFoundException {
        // Retrives the authenticated user
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // Finds the user by Id
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        // Verify if the email of the user matches the one authenticated
        if (!userEntity.getEmail().equals(authenticatedEmail)) {
            return new ResponseEntity<>("You are not authorized to delete this users data", HttpStatus.FORBIDDEN);
        }
        // Deletes user
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
//    public  ResponseEntity<?> deleteUserById(@PathVariable Long id) throws UserTaskNotFoundException {
//        userService.deleteUserById(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}



//    @PostMapping("/submit")
//    @Operation(summary = "Creates a new user", description = "Receives an email, username and password, creates a new user and returns a confirmation message.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "User successfully created."),
//            @ApiResponse(responseCode = "409", description = "Bad request, invalid data.")
//    })
//
//    public ResponseEntity<?> createNewUser(@RequestBody NewUser newUser) throws BadLogInUpdateException {
//        if (newUser.email() == null || newUser.email().isBlank()) {
//            throw new BadLogInUpdateException("Email cannot be null or blank");
//        }
//        if (newUser.username() == null || newUser.username().isBlank()) {
//            throw new BadLogInUpdateException("Username cannot be null or blank");
//        }
//        if (newUser.password() == null || newUser.password().isBlank()) {
//            throw new BadLogInUpdateException("Password cannot be null or blank");
//        }
//        if(userRepository.findByEmail(newUser.email()) != null) {
//            throw new BadLogInUpdateException("Email is already registered");
//        }
//        userService.createNewUser(newUser);
//        return new ResponseEntity<>("User crated succesfully", HttpStatus.CREATED);
//    }
