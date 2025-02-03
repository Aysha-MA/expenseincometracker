package com.project.income.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.income.dto.IncomeDTO;
import com.project.income.entity.Income;
import com.project.income.service.IncomeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/income")
public class IncomeController {

	private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService=incomeService;
    }

	@PostMapping("/add")
	public Income addIncome(@RequestBody @Valid IncomeDTO incomeDTO) {
		return incomeService.addIncome(incomeDTO);
	}

	@PutMapping("/update/{userId}/{id}")
	public Income updateIncome(@PathVariable Long id, @PathVariable Long userId,
			@RequestBody @Valid IncomeDTO incomeDTO) {
		return incomeService.updateIncome(id, incomeDTO);
	}

	@DeleteMapping("/delete/{userId}/{id}")
	public String deleteIncome(@PathVariable Long userId, @PathVariable Long id) {
		return incomeService.deleteIncome(id, userId);
	}

	@GetMapping("/get/{userId}/{id}")
	public Income getIncome(@PathVariable Long userId, @PathVariable Long id) {
		return incomeService.getIncome(id, userId);
	}

	@GetMapping("/getAll/{userId}") // mention ?userId=1&page=1&size=10
	public Page<Income> getAllIncome(@PathVariable Long userId, Pageable pageable) {
		return incomeService.getAllIncome(userId, pageable);
	}

	@GetMapping("/get/total/{userId}")
	public Double getTotalIncome(@PathVariable Long userId) {
		return incomeService.getTotalIncome(userId);
	}

	@GetMapping("/get/daterange") // mention ?userId=1&startDate=2025-01-25&endDate=2025-01-28
	public List<Income> getIncomesByUserIdAndDateBetween(@RequestParam("userId") Long userId,
			@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
		return incomeService.getIncomesByUserIdAndDateBetween(userId, startDate, endDate);
	}

}
