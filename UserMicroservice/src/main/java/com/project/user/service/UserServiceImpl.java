package com.project.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.user.dto.UserDTO;
import com.project.user.entity.User;
import com.project.user.exception.UserAlreadyExistsException;
import com.project.user.exception.UserNotFoundException;
import com.project.user.repository.UserRepository;

/**
 * Service implementation for managing users.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final String USER_NOT_FOUND_MESSAGE = "User not found with id ";

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Registers a new user. If a user with the same userName or email already
	 * exists, a UserAlreadyExistsException is thrown.
	 *
	 * @param userDTO the data transfer object containing the user details
	 * @return the registered user
	 * @throws UserAlreadyExistsException if a user with the same userName or email
	 *                                    already exists
	 */
	@Override
	public UserDTO registerUser(UserDTO userDTO) {
		if (userRepository.findByUserName(userDTO.getUserName()) != null
				|| userRepository.findByEmail(userDTO.getEmail()) != null) {
			throw new UserAlreadyExistsException("User with the same username or email already exists");
		}
		User user = new User();
		user.setUserName(userDTO.getUserName());
		user.setPassword(userDTO.getPassword());
		user.setEmail(userDTO.getEmail());
		User savedUser = userRepository.save(user);
		return new UserDTO(savedUser.getUserName(), null, savedUser.getEmail());
	}

	/**
	 * Retrieves a user by ID. If the user is not found, a UserNotFoundException is
	 * thrown.
	 *
	 * @param userId the ID of the user to be retrieved
	 * @return the retrieved user as a UserDTO
	 * @throws UserNotFoundException if the user is not found
	 */
	@Override
	public UserDTO getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
		return new UserDTO(user.getUserName(), null, user.getEmail());
	}

	/**
	 * Retrieves a list of all users.
	 *
	 * @return a list of all users as UserDTOs
	 */
	@Override
	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDTO> userDTOs = new ArrayList<>();
		for (User user : users) {
			userDTOs.add(new UserDTO(user.getUserName(), null, user.getEmail()));
		}
		return userDTOs;
	}

	/**
	 * Updates an existing user. If the user is not found, a UserNotFoundException
	 * is thrown.
	 *
	 * @param userId  the ID of the user to be updated
	 * @param userDTO the data transfer object containing the updated user details
	 * @return the updated user as a UserDTO
	 * @throws UserNotFoundException if the user is not found
	 */
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

	/**
	 * Deletes a user by ID. If the user is not found, a UserNotFoundException is
	 * thrown.
	 *
	 * @param userId the ID of the user to be deleted
	 * @return a message indicating the result of the deletion
	 * @throws UserNotFoundException if the user is not found
	 */
	@Override
	public String deleteUser(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
		userRepository.delete(user);
		return "User Deleted Successfully";
	}
}
