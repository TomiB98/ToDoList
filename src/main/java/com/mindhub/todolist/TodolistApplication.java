package com.mindhub.todolist;

import com.mindhub.todolist.models.TaskEntity;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.TaskRepository;
import com.mindhub.todolist.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserRepository userRepository, TaskRepository taskRepository) {
		return args -> {

			System.out.println("Server Running!");

//			UserEntity user = new UserEntity("tomas@gmail.com", "Tomas", "123456");
//			userRepository.save(user);
//			System.out.println(user);

//			UserEntity user1 = new UserEntity("manubal@gmail.com", "Manuel", "abcdef");
//			userRepository.save(user1);
//			System.out.println(user1);

//			UserEntity user2 = userRepository.findById(1L).orElse(null);/
//			System.out.println(user2);

//			TaskEntity task = new TaskEntity("TrashOut","Take the trash bag out", TaskStatus.PENDING);
//			user.addTask(task);
//			taskRepository.save(task);
//			System.out.println(task);
		};
	}
}
