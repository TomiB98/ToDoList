package com.mindhub.todolist.repositories;

import com.mindhub.todolist.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

}

//    boolean existByEmail(String email);
//    List<UserEntity> findByUsername(String username);
