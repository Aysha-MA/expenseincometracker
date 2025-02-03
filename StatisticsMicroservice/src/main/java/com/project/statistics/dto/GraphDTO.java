package com.project.statistics.dto;

import java.util.List;

import lombok.Data;

@Data
public class GraphDTO {
	private List<ExpenseDTO> expenseList;
	private List<IncomeDTO> incomeList;
}
