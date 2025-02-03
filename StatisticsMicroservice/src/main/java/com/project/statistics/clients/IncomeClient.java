package com.project.statistics.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.statistics.dto.IncomeDTO;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "INCOMEMICROSERVICE")
public interface IncomeClient {
	@GetMapping("income/get/total/{userId}")
	public Double getTotalIncome(@PathVariable Long userId);

	@GetMapping("income/get/daterange") // mention ?userId=1&startDate=2025-01-25&endDate=2025-01-28
	public List<IncomeDTO> getIncomesByUserIdAndDateBetween(@RequestParam("userId") Long userId,
			@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate);
}
