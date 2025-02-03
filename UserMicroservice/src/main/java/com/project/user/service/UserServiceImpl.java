package com.project.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.user.dto.UserDTO;
import com.project.user.entity.User;
import com.project.user.exception.UserAlreadyExistsException;
import com.project.user.exception.UserNotFoundException;
import com.project.user.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private static final String USER_NOT_FOUND_MESSAGE = "User not found with id ";

	private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        if (userRepository.findByUserName(userDTO.getUserName()) != null
                || userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with the same username or email already exists");
        }
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        return userRepository.save(user);
      } 
    
    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        return new UserDTO(user.getUserName(), null, user.getEmail());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(new UserDTO(user.getUserName(), null, user.getEmail()));
        }
        return userDTOs;
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        User updatedUser = userRepository.save(user);
        return new UserDTO(updatedUser.getUserName(), null, updatedUser.getEmail());
    }

    @Override
    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        userRepository.delete(user);
        return "User Deleted Successfully";
    }
}
