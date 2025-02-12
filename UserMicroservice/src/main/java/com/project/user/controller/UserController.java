package com.project.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.user.dto.UserDTO;
import com.project.user.service.UserService;

import jakarta.validation.Valid;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Registers a new user.
	 *
	 * @param userDTO the data transfer object containing the user details
	 * @return the registered user
	 */
	@PostMapping("/register")
	public UserDTO registerUser(@Valid @RequestBody UserDTO userDTO) {
		return userService.registerUser(userDTO);
	}

	/**
	 * Retrieves a user by ID.
	 *
	 * @param userId the ID of the user to be retrieved
	 * @return the retrieved user as a UserDTO
	 */
	@GetMapping("/getUser/{userId}")
	public UserDTO getUserById(@PathVariable Long userId) {
		return userService.getUserById(userId);
	}

	/**
	 * Retrieves a list of all users.
	 *
	 * @return a list of all users as UserDTOs
	 */
	@GetMapping("/getAll")
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers();
	}

	/**
	 * Updates an existing user.
	 *
	 * @param userId  the ID of the user to be updated
	 * @param userDTO the data transfer object containing the updated user details
	 * @return the updated user as a UserDTO
	 */
	@PutMapping("/update/{userId}")
	public UserDTO updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
		return userService.updateUser(userId, userDTO);
	}

	/**
	 * Deletes a user by ID.
	 *
	 * @param userId the ID of the user to be deleted
	 * @return a message indicating the result of the deletion
	 */
	@DeleteMapping("/delete/{userId}")
	public String deleteUser(@PathVariable Long userId) {
		return userService.deleteUser(userId);
	}
}
