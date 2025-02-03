package com.project.expense.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private static final String EXPENSE_NOT_FOUND_MESSAGE = "Expense Not Found";

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public String deleteExpense(Long id, Long userId) {
        Optional<Expense> optionalExpense = expenseRepository.findByIdAndUserId(id, userId);
        if (optionalExpense.isPresent()) {
            expenseRepository.delete(optionalExpense.get());
            return "Expense Deleted Successfully";
        }
        throw new ExpenseNotFoundException(EXPENSE_NOT_FOUND_MESSAGE);
    }

    @Override
    public Expense getExpense(Long id, Long userId) {
        return expenseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ExpenseNotFoundException(EXPENSE_NOT_FOUND_MESSAGE));
    }

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

    @Override
    public Double getTotalExpenses(Long userId) {
        Double totalExpenses = expenseRepository.sumAmountByUserId(userId);
        if (totalExpenses == null) {
            throw new ExpenseNotFoundException(EXPENSE_NOT_FOUND_MESSAGE);
        }
        return totalExpenses;
    }

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

    @Override
    public List<Expense> getExpenseByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        if (expenses.isEmpty()) {
            throw new ExpenseNotFoundException("No expenses found for the given date range");
        }
        return expenses;
    }
}
