package com.poseidoncapitalsolutions.trading.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poseidoncapitalsolutions.trading.dto.UserCreateDTO;
import com.poseidoncapitalsolutions.trading.dto.UserUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.UserListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.UserNotFoundException;
import com.poseidoncapitalsolutions.trading.model.User;
import com.poseidoncapitalsolutions.trading.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<UserListItemDTO> getAllUsers(){
        return userRepository.findAll().stream()
                    .map(user -> new UserListItemDTO(user.getId(), user.getFullname(), user.getUsername(), user.getRole()))
                    .toList();
    }
    
    public User createUser(UserCreateDTO userCreateDTO){
        log.debug("Creating user from DTO: {}", userCreateDTO);

        User newUser = new User();
        newUser.setUsername(userCreateDTO.username());
        newUser.setFullname(userCreateDTO.fullname());
        newUser.setRole(userCreateDTO.role());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        newUser.setPassword(encoder.encode(userCreateDTO.rawPassword()));

        User savedUser = userRepository.save(newUser);
        log.info("User successfully created with ID[{}]", savedUser.getId());
        
        return savedUser;
    }

    public User updateUser(UserUpdateDTO userUpdateDTO){
        log.debug("Updating user from DTO: {}", userUpdateDTO);

        User user = userRepository.findById(userUpdateDTO.id())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setUsername(userUpdateDTO.username());
        user.setFullname(userUpdateDTO.fullname());
        user.setRole(userUpdateDTO.role());

        // todo: voir si ça convient
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(userUpdateDTO.rawPassword()));

        User updatedUser = userRepository.save(user);
        log.info("User successfully updated with ID[{}]", updatedUser.getId());
        
        return userRepository.save(updatedUser);
    }

    public void deleteById(int id) {
        log.debug("Deleting user with id: {}", id);
    
        User userToDelete = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    
        userRepository.delete(userToDelete);
        log.info("User successfully deleted with id: {}", id);
    }

    public UserUpdateDTO getUserUpdateDTO(int id){
        log.debug("Creating UserUpdateDTO for ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        return new UserUpdateDTO(user.getId(), user.getUsername(), "", user.getFullname(), user.getRole());
    }
}
