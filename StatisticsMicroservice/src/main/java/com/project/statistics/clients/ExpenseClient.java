package com.project.statistics.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.statistics.dto.ExpenseDTO;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "EXPENSEMICROSERVICE")
public interface ExpenseClient {
	@GetMapping("expense/get/total/{userId}")
	public Double getTotalExpenses(@PathVariable Long userId);

	@GetMapping("expense/get/daterange") // mention ?userId=1&startDate=2025-01-25&endDate=2025-01-28
	public List<ExpenseDTO> getExpenseByUserIdAndDateBetween(@RequestParam("userId") Long userId,
			@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate);
}
