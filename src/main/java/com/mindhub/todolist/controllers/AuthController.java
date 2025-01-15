package com.mindhub.todolist.controllers;

import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.dtos.NewUser;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginUser loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody NewUser newUser) {

        if (newUser.email() == null || newUser.email().isBlank()) {
            return new ResponseEntity<>("Email cannot be null or blank", HttpStatus.BAD_REQUEST);
        }

        if (newUser.username() == null || newUser.username().isBlank()) {
            return new ResponseEntity<>("Username cannot be null or blank", HttpStatus.BAD_REQUEST);
        }

        if (newUser.password() == null || newUser.password().isBlank()) {
            return new ResponseEntity<>("Password cannot be null or blank", HttpStatus.BAD_REQUEST);
        }

        if(userRepository.findByEmail(newUser.email()).isPresent()) {
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }

        userService.createNewUser(newUser);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }
}
