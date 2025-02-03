package com.project.statistics.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ExpenseDTO {
	private Long id;
	private Long userId;
	private Double amount;
	private LocalDate date;

}
