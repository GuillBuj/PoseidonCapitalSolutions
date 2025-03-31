package poseidoncapitalsolutions.trading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.poseidoncapitalsolutions.trading.dto.UserCreateDTO;
import com.poseidoncapitalsolutions.trading.dto.UserUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.UserListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.UserNotFoundException;
import com.poseidoncapitalsolutions.trading.exception.UsernameAlreadyExistsException;
import com.poseidoncapitalsolutions.trading.mapper.UserMapper;
import com.poseidoncapitalsolutions.trading.model.User;
import com.poseidoncapitalsolutions.trading.repository.UserRepository;
import com.poseidoncapitalsolutions.trading.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setFullname("Test User");
        user.setRole("USER");
    }

    @Test
    void getAllUsersOk() {
        UserListItemDTO expectedListItemDTO = new UserListItemDTO(1, "testuser", "Test User", "USER");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.toListItemDTOList(anyList())).thenReturn(Arrays.asList(expectedListItemDTO));
        
        List<UserListItemDTO> result = userService.getAllUsers();
        
        assertEquals(1, result.size());
        assertEquals(expectedListItemDTO, result.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void createUserOk() {
        UserCreateDTO createDTO = new UserCreateDTO("newuser", "ValidPass1!", "New User", "ADMIN");
        
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userMapper.toEntity(createDTO)).thenReturn(user);
        when(passwordEncoder.encode("ValidPass1!")).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);
        
        User result = userService.createUser(createDTO);
        
        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void createUserShouldThrowWhenUsernameExists() {
        UserCreateDTO createDTO = new UserCreateDTO("existinguser", "ValidPass1!", "Existing User", "USER");
        
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
        
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(createDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserOk() {
        UserUpdateDTO updateDTO = new UserUpdateDTO(1, "updateduser", "NewPass1!", "Updated User", "ADMIN");
        
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("NewPass1!")).thenReturn("newEncodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        
        User result = userService.updateUser(updateDTO);
        
        assertEquals(1, result.getId());
        assertEquals("newEncodedPassword", user.getPassword());
        verify(userMapper).updateUserFromDTO(updateDTO, user);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserNotFound() {
        int nonExistentId = 999;
        UserUpdateDTO updateDTO = new UserUpdateDTO(nonExistentId, "user", "pass", "name", "role");
        
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteByIdOk() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        
        userService.deleteById(1);
        
        verify(userRepository).delete(user);
    }

    @Test
    void deleteByIdNotFound() {
        int nonExistentId = 999;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> userService.deleteById(nonExistentId));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void getUserUpdateDTOOk() {
        UserUpdateDTO expectedUpdateDTO = new UserUpdateDTO(1, "testuser", "", "Test User", "USER");
        
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        
        UserUpdateDTO result = userService.getUserUpdateDTO(1);
        
        assertEquals(expectedUpdateDTO, result);
        verify(userRepository).findById(1);
    }

    @Test
    void getUserUpdateDTONotFound() {
        int nonExistentId = 999;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> userService.getUserUpdateDTO(nonExistentId));
    }
}
