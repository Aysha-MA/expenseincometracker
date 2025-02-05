package com.project.expense.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ExpenseDTO {
	@NotBlank(message = "Title is mandatory")
	private String title;

	private String description;

	@NotBlank(message = "Category is mandatory")
	private String category;

	@NotNull(message = "Amount is mandatory")
	private Double amount;

	@NotNull(message = "Date is mandatory")
	private LocalDate date;

	@NotNull(message = "User ID is mandatory")
	private Long userId;
}
