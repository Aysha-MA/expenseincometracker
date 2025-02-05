package com.project.expense.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "expense")
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	private String description;

	@Column(nullable = false)
	private String category;

	@Column(nullable = false)
	private Double amount;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private Long userId; // Reference to the user
}
