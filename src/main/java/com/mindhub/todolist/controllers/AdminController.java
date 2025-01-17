package com.mindhub.todolist.controllers;

import com.mindhub.todolist.dtos.UpdateUser;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<String> noIdProvided() {
        return ResponseEntity.badRequest().body("The provided ID cannot be null or empty.");
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Returns a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List successfully retrieved."),
            @ApiResponse(responseCode = "403", description = "Access denied.")
    })
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets the user data with the id", description = "Receives an id and returns all the data of the specified user + all his tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data successfully received."),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid id.")
    })
    public UserDTO getUserById(@PathVariable Long id) throws UserTaskNotFoundException {
        return userService.getUserDTOById(id);
    };

    @PutMapping("update/{id}")
    @Operation(summary = "Updates a user", description = "Receives an id and updates the assigned user, you can update username or password independently, if you leave one blank it will retrieve the old value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully updated."),
            @ApiResponse(responseCode = "409", description = "Bad request, user not found.")
    })
    public ResponseEntity<?> updateUserById(@RequestBody UpdateUser updateUser, @PathVariable Long id) throws Exception {
        UserDTO updatedUser = userService.updateUserById(updateUser, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Receives an id and and deletes the assigned user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted."),
            @ApiResponse(responseCode = "400", description = "Bad request, user not found.")
    })
    public  ResponseEntity<?> deleteUserById(@PathVariable Long id) throws UserTaskNotFoundException {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
