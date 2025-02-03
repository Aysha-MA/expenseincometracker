package com.project.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.user.dto.UserDTO;
import com.project.user.entity.User;
import com.project.user.exception.UserAlreadyExistsException;
import com.project.user.exception.UserNotFoundException;
import com.project.user.repository.UserRepository;
import com.project.user.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserMicroserviceApplicationTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO("testUser", "password", "test@example.com");
        user = new User();
        user.setUserId(1L);
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
    }

    @Test
    public void testRegisterUser_Success() {
        when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(null);
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(userDTO);
        assertNotNull(result);
        assertEquals(user.getUserName(), result.getUserName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(new User());

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(userDTO);
        });
    }

    @Test
    public void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });
    }

    @Test
    public void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.deleteUser(1L);
        assertEquals("User Deleted Successfully", result);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });
    }
}
