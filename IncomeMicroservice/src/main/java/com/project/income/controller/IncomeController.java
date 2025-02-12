package com.project.income.controller;

import java.time.LocalDate;
import java.util.List;

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

/**
 * REST controller for managing incomes.
 */
@RestController
@RequestMapping("/income")
public class IncomeController {

	private final IncomeService incomeService;

	public IncomeController(IncomeService incomeService) {
		this.incomeService = incomeService;
	}

	/**
	 * Adds a new income.
	 *
	 * @param incomeDTO the data transfer object containing the income details
	 * @return the added income
	 */
	@PostMapping("/add")
	public Income addIncome(@RequestBody @Valid IncomeDTO incomeDTO) {
		return incomeService.addIncome(incomeDTO);
	}

	/**
	 * Updates an existing income.
	 *
	 * @param id        the ID of the income to be updated
	 * @param userId    the ID of the user who owns the income
	 * @param incomeDTO the data transfer object containing the updated income
	 *                  details
	 * @return the updated income
	 */
	@PutMapping("/update/{userId}/{id}")
	public Income updateIncome(@PathVariable Long id, @PathVariable Long userId,
			@RequestBody @Valid IncomeDTO incomeDTO) {
		return incomeService.updateIncome(id, incomeDTO);
	}

	/**
	 * Deletes an income.
	 *
	 * @param userId the ID of the user who owns the income
	 * @param id     the ID of the income to be deleted
	 * @return a message indicating the result of the deletion
	 */
	@DeleteMapping("/delete/{userId}/{id}")
	public String deleteIncome(@PathVariable Long userId, @PathVariable Long id) {
		return incomeService.deleteIncome(id, userId);
	}

	/**
	 * Retrieves an income by ID.
	 *
	 * @param userId the ID of the user who owns the income
	 * @param id     the ID of the income to be retrieved
	 * @return the retrieved income
	 */
	@GetMapping("/get/{userId}/{id}")
	public Income getIncome(@PathVariable Long userId, @PathVariable Long id) {
		return incomeService.getIncome(id, userId);
	}

	/**
	 * Retrieves a paginated list of all incomes for a specific user.
	 *
	 * @param userId   the ID of the user whose incomes are to be retrieved
	 * @param pageable the pagination information
	 * @return a paginated list of incomes
	 */
	@GetMapping("/getAll/{userId}")
	public Page<Income> getAllIncome(@PathVariable Long userId, Pageable pageable) {
		return incomeService.getAllIncome(userId, pageable);
	}

	/**
	 * Retrieves the total amount of incomes for a specific user.
	 *
	 * @param userId the ID of the user whose total incomes are to be retrieved
	 * @return the total amount of incomes
	 */
	@GetMapping("/get/total/{userId}")
	public Double getTotalIncome(@PathVariable Long userId) {
		return incomeService.getTotalIncome(userId);
	}

	/**
	 * Retrieves a list of incomes for a specific user within a given date range.
	 *
	 * @param userId    the ID of the user whose incomes are to be retrieved
	 * @param startDate the start date of the date range
	 * @param endDate   the end date of the date range
	 * @return a list of incomes for the specified user and date range
	 */
	@GetMapping("/get/daterange")
	public List<Income> getIncomesByUserIdAndDateBetween(@RequestParam("userId") Long userId,
			@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
		return incomeService.getIncomesByUserIdAndDateBetween(userId, startDate, endDate);
	}
}
