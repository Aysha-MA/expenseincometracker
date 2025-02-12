package com.project.expense.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.expense.dto.ExpenseDTO;
import com.project.expense.entity.Expense;
import com.project.expense.exception.ExpenseNotFoundException;
import com.project.expense.repository.ExpenseRepository;

/**
 * Service implementation for managing incomes.
 */
@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

	private static final String EXPENSE_NOT_FOUND_MESSAGE = "Expense Not Found";

	private final ExpenseRepository expenseRepository;

	public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}

	/**
	 * Adds a new expense for a specific user.
	 *
	 * @param expenseDTO the data transfer object containing the expense details
	 * @return the added expense
	 */
	@Override
	public Expense addExpense(ExpenseDTO expenseDTO) {
		Expense expense = new Expense();
		expense.setTitle(expenseDTO.getTitle());
		expense.setDescription(expenseDTO.getDescription());
		expense.setCategory(expenseDTO.getCategory());
		expense.setAmount(expenseDTO.getAmount());
		expense.setDate(expenseDTO.getDate());
		expense.setUserId(expenseDTO.getUserId());
		return expenseRepository.save(expense);
	}

	/**
	 * Deletes an expense for a specific user by expense ID. If the expense is not
	 * found, an ExpenseNotFoundException is thrown.
	 *
	 * @param id     the ID of the expense to be deleted
	 * @param userId the ID of the user who owns the expense
	 * @return a message indicating the result of the deletion
	 * @throws ExpenseNotFoundException if the expense is not found
	 */
	@Override
	public String deleteExpense(Long id, Long userId) {
		Optional<Expense> optionalExpense = expenseRepository.findByIdAndUserId(id, userId);
		if (optionalExpense.isPresent()) {
			expenseRepository.delete(optionalExpense.get());
			return "Expense Deleted Successfully";
		}
		throw new ExpenseNotFoundException(EXPENSE_NOT_FOUND_MESSAGE);
	}

	/**
	 * Retrieves an expense for a specific user by expense ID. If the expense is not
	 * found, an ExpenseNotFoundException is thrown.
	 *
	 * @param id     the ID of the expense to be retrieved
	 * @param userId the ID of the user who owns the expense
	 * @return the retrieved expense
	 * @throws ExpenseNotFoundException if the expense is not found
	 */
	@Override
	public Expense getExpense(Long id, Long userId) {
		Optional<Expense> optionalExpense = expenseRepository.findByIdAndUserId(id, userId);
		if (optionalExpense.isPresent()) {
			return optionalExpense.get();
		} else {
			throw new ExpenseNotFoundException(EXPENSE_NOT_FOUND_MESSAGE);
		}
	}

	/**
	 * Retrieves a paginated list of all expenses for a specific user. If the
	 * pageable parameter is null, a default pageable is used.
	 *
	 * @param userId   the ID of the user whose expenses are to be retrieved
	 * @param pageable the pagination information
	 * @return a paginated list of expenses
	 */
	@Override
	public Page<Expense> getAllExpenses(Long userId, Pageable pageable) {
		if (pageable == null) {
			pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("date")));
		} else {
			pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
					Sort.by(Sort.Order.desc("date")));
		}
		return expenseRepository.findByUserId(userId, pageable);
	}

	/**
	 * Retrieves the total amount of expenses for a specific user. If no expenses
	 * are found, an ExpenseNotFoundException is thrown.
	 *
	 * @param userId the ID of the user whose total expenses are to be retrieved
	 * @return the total amount of expenses
	 * @throws ExpenseNotFoundException if no expenses are found
	 */
	@Override
	public Double getTotalExpenses(Long userId) {
		Double totalExpenses = expenseRepository.sumAmountByUserId(userId);
		if (totalExpenses == null) {
			throw new ExpenseNotFoundException(EXPENSE_NOT_FOUND_MESSAGE);
		}
		return totalExpenses;
	}

	/**
	 * Updates an existing expense for a specific user. If the expense is not found,
	 * an ExpenseNotFoundException is thrown.
	 *
	 * @param id         the ID of the expense to be updated
	 * @param expenseDTO the data transfer object containing the updated expense
	 *                   details
	 * @return the updated expense
	 * @throws ExpenseNotFoundException if the expense is not found
	 */
	@Override
	public Expense updateExpense(Long id, ExpenseDTO expenseDTO) {
		Optional<Expense> optionalExpense = expenseRepository.findByIdAndUserId(id, expenseDTO.getUserId());
		if (optionalExpense.isPresent()) {
			Expense expense = optionalExpense.get();
			if (expenseDTO.getTitle() != null)
				expense.setTitle(expenseDTO.getTitle());
			if (expenseDTO.getDescription() != null)
				expense.setDescription(expenseDTO.getDescription());
			if (expenseDTO.getCategory() != null)
				expense.setCategory(expenseDTO.getCategory());
			if (expenseDTO.getAmount() != null)
				expense.setAmount(expenseDTO.getAmount());
			if (expenseDTO.getDate() != null)
				expense.setDate(expenseDTO.getDate());
			return expenseRepository.save(expense);
		}
		throw new ExpenseNotFoundException(EXPENSE_NOT_FOUND_MESSAGE);
	}

	/**
	 * Retrieves a list of expenses for a specific user within a given date range.
	 * If no expenses are found, an ExpenseNotFoundException is thrown.
	 *
	 * @param userId    the ID of the user whose expenses are to be retrieved
	 * @param startDate the start date of the date range
	 * @param endDate   the end date of the date range
	 * @return a list of expenses for the specified user and date range
	 * @throws ExpenseNotFoundException if no expenses are found for the given date
	 *                                  range
	 */
	@Override
	public List<Expense> getExpenseByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
		List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
		if (expenses.isEmpty()) {
			throw new ExpenseNotFoundException("No expenses found for the given date range");
		}
		return expenses;
	}
}
