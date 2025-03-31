package com.poseidoncapitalsolutions.trading.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidoncapitalsolutions.trading.dto.UserCreateDTO;
import com.poseidoncapitalsolutions.trading.dto.UserUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.UserListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.UserNotFoundException;
import com.poseidoncapitalsolutions.trading.exception.UsernameAlreadyExistsException;
import com.poseidoncapitalsolutions.trading.mapper.UserMapper;
import com.poseidoncapitalsolutions.trading.model.User;
import com.poseidoncapitalsolutions.trading.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public List<UserListItemDTO> getAllUsers(){
        return userMapper.toListItemDTOList(userRepository.findAll());
    }
    
    public User createUser(UserCreateDTO userCreateDTO){
        log.debug("Creating user from DTO: {}", userCreateDTO);

        if(userRepository.existsByUsername(userCreateDTO.username())){
            log.error("Username already exists: {}", userCreateDTO.username());
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        User newUser = userMapper.toEntity(userCreateDTO);

        newUser.setPassword(passwordEncoder.encode(userCreateDTO.rawPassword()));

        User savedUser = userRepository.save(newUser);
        log.info("User successfully created with ID[{}]", savedUser.getId());
        
        return savedUser;
    }

    public User updateUser(UserUpdateDTO userUpdateDTO){
        log.debug("Updating user from DTO: {}", userUpdateDTO);

        User user = userRepository.findById(userUpdateDTO.id())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

                userMapper.updateUserFromDTO(userUpdateDTO, user);

        user.setPassword(passwordEncoder.encode(userUpdateDTO.rawPassword()));
        
        User updatedUser = userRepository.save(user);
        log.info("User successfully updated with ID[{}]", updatedUser.getId());
        
        return updatedUser;
    }

    public void deleteById(int id) {
        log.debug("Deleting user with id: {}", id);
    
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    
        userRepository.delete(user);
        log.info("User successfully deleted with id: {}", id);
    }

    public UserUpdateDTO getUserUpdateDTO(int id){
        log.debug("Creating UserUpdateDTO for ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        return new UserUpdateDTO(user.getId(), user.getUsername(), "", user.getFullname(), user.getRole());
    }
}
