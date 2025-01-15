package com.mindhub.todolist.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String email;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "user")
    private Set<TaskEntity> tasks = new HashSet<>();

    public UserEntity() {}

    public UserEntity(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public UserEntity(String email, String username, String password, RoleType roleType) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = roleType;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTask(Set<TaskEntity> task) {
        this.tasks = task;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}

//import com.mind hub.todolist.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;

//    public void addTask(TaskEntity task) {
//        task.setUser(this);
//        tasks.add(task);
//    }

//    @Override
//    public String toString() {
//        return "UserEntity{" +
//                "id=" + id +
//                ", email='" + email + '\'' +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                '}';
//    }
