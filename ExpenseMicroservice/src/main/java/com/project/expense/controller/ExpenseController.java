package com.project.expense.controller;

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

import com.project.expense.dto.ExpenseDTO;
import com.project.expense.entity.Expense;
import com.project.expense.service.ExpenseService;

import jakarta.validation.Valid;

/**
 * REST controller for managing expenses.
 */
@RestController
@RequestMapping("/expense")
public class ExpenseController {

	private final ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	/**
	 * Adds a new expense.
	 *
	 * @param expenseDTO the data transfer object containing the expense details
	 * @return the added expense
	 */
	@PostMapping("/add")
	public Expense addExpense(@RequestBody @Valid ExpenseDTO expenseDTO) {
		return expenseService.addExpense(expenseDTO);
	}

	/**
	 * Updates an existing expense.
	 *
	 * @param id         the ID of the expense to be updated
	 * @param userId     the ID of the user who owns the expense
	 * @param expenseDTO the data transfer object containing the updated expense
	 *                   details
	 * @return the updated expense
	 */
	@PutMapping("/update/{userId}/{id}")
	public Expense updateExpense(@PathVariable Long id, @PathVariable Long userId,
			@RequestBody @Valid ExpenseDTO expenseDTO) {
		return expenseService.updateExpense(id, expenseDTO);
	}

	/**
	 * Deletes an expense.
	 *
	 * @param userId the ID of the user who owns the expense
	 * @param id     the ID of the expense to be deleted
	 * @return a message indicating the result of the deletion
	 */
	@DeleteMapping("/delete/{userId}/{id}")
	public String deleteExpense(@PathVariable Long userId, @PathVariable Long id) {
		return expenseService.deleteExpense(id, userId);
	}

	/**
	 * Retrieves an expense by ID.
	 *
	 * @param userId the ID of the user who owns the expense
	 * @param id     the ID of the expense to be retrieved
	 * @return the retrieved expense
	 */
	@GetMapping("/get/{userId}/{id}")
	public Expense getExpense(@PathVariable Long userId, @PathVariable Long id) {
		return expenseService.getExpense(id, userId);
	}

	/**
	 * Retrieves a paginated list of all expenses for a specific user.
	 *
	 * @param userId   the ID of the user whose expenses are to be retrieved
	 * @param pageable the pagination information
	 * @return a paginated list of expenses
	 */
	@GetMapping("/getAll/{userId}")
	public Page<Expense> getAllExpenses(@PathVariable Long userId, Pageable pageable) {
		return expenseService.getAllExpenses(userId, pageable);
	}

	/**
	 * Retrieves the total amount of expenses for a specific user.
	 *
	 * @param userId the ID of the user whose total expenses are to be retrieved
	 * @return the total amount of expenses
	 */
	@GetMapping("/get/total/{userId}")
	public Double getTotalExpenses(@PathVariable Long userId) {
		return expenseService.getTotalExpenses(userId);
	}

	/**
	 * Retrieves a list of expenses for a specific user within a given date range.
	 *
	 * @param userId    the ID of the user whose expenses are to be retrieved
	 * @param startDate the start date of the date range
	 * @param endDate   the end date of the date range
	 * @return a list of expenses for the specified user and date range
	 */
	@GetMapping("/get/daterange")
	public List<Expense> getExpenseByUserIdAndDateBetween(@RequestParam("userId") Long userId,
			@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
		return expenseService.getExpenseByUserIdAndDateBetween(userId, startDate, endDate);
	}
}
