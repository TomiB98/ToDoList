package com.mindhub.todolist.impl;

import com.mindhub.todolist.dtos.NewUser;
import com.mindhub.todolist.dtos.UpdateUser;
import com.mindhub.todolist.dtos.UserDTO;
import com.mindhub.todolist.exeptions.BadLogInUpdateException;
import com.mindhub.todolist.exeptions.UserTaskNotFoundException;
import com.mindhub.todolist.models.RoleType;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserEntity;
import com.mindhub.todolist.repositories.UserRepository;
import com.mindhub.todolist.services.UserService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.mindhub.todolist.utilitary.ValidationUtils.PASSWORD_PATTERN;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserDTOById(Long id) throws UserTaskNotFoundException {
        return new UserDTO(getUserById(id));
    }

    @Override
    public UserEntity getUserById(Long id) throws UserTaskNotFoundException {
        return userRepository.findById(id).orElseThrow( () -> new UserTaskNotFoundException("User with ID " + id + " not found."));
    }

    @Override
    public void createNewUser(NewUser newUser) {
        RoleType role = RoleType.valueOf(newUser.role());
        UserEntity user = new UserEntity(newUser.email(), newUser.username(), passwordEncoder.encode(newUser.password()), role);
        UserEntity savedUser = saveUser(user);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserDTO updateUserById(UpdateUser updatedUser, Long id) throws Exception {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new BadLogInUpdateException("User with ID " + id + " not found."));

        if (!updatedUser.username().isBlank()) {
            user.setUsername(updatedUser.username());
        }

        if (!updatedUser.password().isBlank()) {
            validateUpdatedUser(updatedUser, user);
            user.setPassword(passwordEncoder.encode(updatedUser.password()));
        }

        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Override
    public void deleteUserById(Long id) throws UserTaskNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(()-> new UserTaskNotFoundException("User with ID " + id + " not found."));
        userRepository.deleteById(id);
    }

    // Validations //

    public void validateUpdatedUser (UpdateUser updateUser, UserEntity user) throws Exception {
        validateEqualPassword(updateUser.password(), user.getPassword());
        validateUpdatedPassword(updateUser.password());
    }

    public static void validateUpdatedPassword (String password) throws BadLogInUpdateException {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if(!matcher.matches()) {
            throw new BadLogInUpdateException("Password must have at lest: one digit, a lower and upper case letter, a special character, 8 characters and no whitespace.");
        }
    }

    public void validateEqualPassword (String updatedPassword, String password) throws ValidationException, BadLogInUpdateException {
        if (passwordEncoder.matches(updatedPassword, password)) {
                throw new BadLogInUpdateException("New password must be different to the old one.");
            }
    }
}



//            if (passwordEncoder.matches(updatedUser.password(), user.getPassword())) {
//                throw new BadLogInUpdateException("New password must be different to the old one.");
//            }
//            Matcher matcher = PASSWORD_PATTERN.matcher(updatedUser.password());
//            if(!matcher.matches()) {
//                throw new BadLogInUpdateException("Password must have at lest: one digit, a lower and upper case letter, a special character, 8 characters and no whitespace.");
//            } else user.setPassword(passwordEncoder.encode(updatedUser.password()));

//            if(updatedUser.username().isBlank()) {
//                user.setUsername(user.getUsername());
//            } else user.setUsername(updatedUser.username());
//
//            if(updatedUser.password().isBlank()) {
//                user.setPassword(user.getPassword());
//            } else {
//                validateUpdatedUser(updatedUser, user);
//                user.setPassword(passwordEncoder.encode(updatedUser.password()));
//            }