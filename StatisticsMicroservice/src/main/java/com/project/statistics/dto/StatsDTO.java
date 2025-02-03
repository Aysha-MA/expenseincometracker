package com.project.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsDTO {
	private Double totalIncome;
	private Double totalExpense;
	private Double balance;
}
