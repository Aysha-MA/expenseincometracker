package com.project.statistics.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "stats")
public class Stats {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "total_income")
	private Double totalIncome;

	@Column(name = "total_expense")
	private Double totalExpense;

	@Column(name = "balance")
	private Double balance;
}
