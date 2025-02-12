package com.project.user.service;

import java.util.List;
import com.project.user.dto.UserDTO;

public interface UserService {
	public abstract UserDTO registerUser(UserDTO userDTO);

	public abstract UserDTO getUserById(Long userId);

	public abstract List<UserDTO> getAllUsers();

	public abstract UserDTO updateUser(Long userId, UserDTO userDTO);

	public abstract String deleteUser(Long userId);
}
