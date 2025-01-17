package com.mindhub.todolist.controllers;

import com.mindhub.todolist.config.JwtUtils;
import com.mindhub.todolist.dtos.LoginUser;
import com.mindhub.todolist.dtos.NewUser;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import jakarta.validation.ValidationException;
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

import java.util.regex.Matcher;

import static com.mindhub.todolist.utilitary.ValidationUtils.EMAIL_PATTERN;
import static com.mindhub.todolist.utilitary.ValidationUtils.PASSWORD_PATTERN;

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
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(authentication.getName());
            return ResponseEntity.ok(jwt);

        } catch (Exception ex) {
            return  new ResponseEntity<>("Password or email invalid.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody NewUser newUser) {

        try {

            validateNewUser(newUser);

            userService.createNewUser(newUser);
            return new ResponseEntity<>("User created.", HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public void validateNewUser (NewUser newUser) throws Exception {
        validateUserName(newUser.username());
        validatePassword(newUser.password());
        validateUserEmail(newUser.email());
        validateIfEmailExist(newUser.email());
    }

    public static void validateUserName (String username) throws ValidationException {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username cannot be null or blank.");
        }
    }

    public static void validatePassword (String password) throws ValidationException {
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password cannot be null or blank.");
        }
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if(!matcher.matches()) {
            throw new ValidationException("Password must have at lest: one digit, a lower and upper case letter, a special character, 8 characters and no whitespace.");
        }
    }

    public static void validateUserEmail (String email) throws ValidationException {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email cannot be null or blank.");
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()) {
            throw new ValidationException("Invalid email format: must have at least 8 characters and a '@'.");
        }
    }

    public void validateIfEmailExist (String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new ValidationException ("This email is already registered.");
        }
    }
}
