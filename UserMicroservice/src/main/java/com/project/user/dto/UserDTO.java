package com.project.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
	@NotNull(message = "Username is mandatory")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	private String userName;

	@NotNull(message = "Password is mandatory")
	@Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
	private String password;

	@NotNull(message = "Email is mandatory")
	@Email(message = "Email should be valid")
	private String email;
}
