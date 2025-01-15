package com.mindhub.todolist.controllers;

import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/user")
public class AppController {

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String getUserName(Authentication authentication) {

        Authentication authentication1 = authentication;

        UserEntity user = userRepository.findByEmail(authentication.getName()).orElse(null);

        System.out.println("hola");
        return authentication.getName();
    }
}
