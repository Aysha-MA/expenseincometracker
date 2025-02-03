package com.project.expense.controller;

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

import com.project.expense.dto.ExpenseDTO;
import com.project.expense.entity.Expense;
import com.project.expense.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
	
	   private final ExpenseService expenseService;

	    @Autowired
	    public ExpenseController(ExpenseService expenseService) {
	        this.expenseService=expenseService;
	    }
	    
	@PostMapping("/add")
	public Expense addExpense(@RequestBody @Valid ExpenseDTO expenseDTO) {
		return expenseService.addExpense(expenseDTO);
	}

	@PutMapping("/update/{userId}/{id}")
	public Expense updateExpense(@PathVariable Long id, @PathVariable Long userId,
			@RequestBody @Valid ExpenseDTO expenseDTO) {
		return expenseService.updateExpense(id, expenseDTO);
	}

	@DeleteMapping("/delete/{userId}/{id}")
	public String deleteExpense(@PathVariable Long userId, @PathVariable Long id) {
		return expenseService.deleteExpense(id, userId);
	}

	@GetMapping("/get/{userId}/{id}")
	public Expense getExpense(@PathVariable Long userId, @PathVariable Long id) {
		return expenseService.getExpense(id, userId);
	}

	@GetMapping("/getAll/{userId}") // mention ?page=1&size=10
	public Page<Expense> getAllExpenses(@PathVariable Long userId, Pageable pageable) {
		return expenseService.getAllExpenses(userId, pageable);
	}

	@GetMapping("/get/total/{userId}")
	public Double getTotalExpenses(@PathVariable Long userId) {
		return expenseService.getTotalExpenses(userId);
	}

	@GetMapping("/get/daterange") // mention ?userId=1&startDate=2025-01-25&endDate=2025-01-28
	public List<Expense> getExpenseByUserIdAndDateBetween(@RequestParam("userId") Long userId,
			@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
		return expenseService.getExpenseByUserIdAndDateBetween(userId, startDate, endDate);
	}
}