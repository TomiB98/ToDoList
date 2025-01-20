package com.mindhub.todolist;

import com.mindhub.todolist.models.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

//	@Autowired
//	private PasswordEncoder passwordEncoder;

//	@Bean
//	public CommandLineRunner initData(UserRepository userRepository, TaskRepository taskRepository) {
//		return args -> {
//
////			UserEntity user = new UserEntity("tomas@gmail.com", "Tomas", passwordEncoder.encode("123456"));
//			UserEntity user = new UserEntity("tomas@gmail.com", "Tomas", passwordEncoder.encode("Tomas123."), RoleType.USER);
//			userRepository.save(user);
//			System.out.println(user);
//
//			UserEntity user1 = new UserEntity("manubal@gmail.com", "Manuel", passwordEncoder.encode("Manuel123."), RoleType.USER);
//			userRepository.save(user1);
//			System.out.println(user1);
//
//			UserEntity user2 = userRepository.findById(1L).orElse(null);
//			System.out.println(user2);
//
////			TaskEntity task = new TaskEntity("TrashOut","Take the trash bag out", TaskStatus.PENDING);
////			user.addTask(task);
//
//			System.out.println("Server Running!");
//		};
//	}
}

//import com.mind hub.todolist.repositories.TaskRepository;
//import com.mind hub.todolist.repositories.UserRepository;
//import com.mind hub.todolist.models.TaskEntity;
//import com.mind hub.todolist.models.TaskStatus;
//import com.mind hub.todolist.models.UserEntity;

// UserRepository , TaskRepository
// userRepository , taskRepository

//			UserEntity user = new UserEntity("tomas@gmail.com", "Tomas", "123456");
//			userRepository.save(user);
//			System.out.println(user);

//			UserEntity user1 = new UserEntity("manubal@gmail.com", "Manuel", "acked");
//			userRepository.save(user1);
//			System.out.println(user1);

//			UserEntity user2 = userRepository.findById(1L).orElse(null);/
//			System.out.println(user2);

//			TaskEntity task = new TaskEntity("TrashOut","Take the trash bag out", TaskStatus.PENDING);
//			user.addTask(task);

//			taskRepository.save(task);
//			System.out.println(task);
//			"title": "TrashOut",
//			"description": "Take the trash bag out",
//			"status": "PENDING",
