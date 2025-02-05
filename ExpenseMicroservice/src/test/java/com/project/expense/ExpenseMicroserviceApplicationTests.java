package com.project.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.project.expense.dto.ExpenseDTO;
import com.project.expense.entity.Expense;
import com.project.expense.exception.ExpenseNotFoundException;
import com.project.expense.repository.ExpenseRepository;
import com.project.expense.service.ExpenseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ExpenseMicroserviceApplicationTests {

	@Mock
	private ExpenseRepository expenseRepository;

	@InjectMocks
	private ExpenseServiceImpl expenseService;

	private Expense expense;
	private ExpenseDTO expenseDTO;

	@BeforeEach
	public void setUp() {
		expense = new Expense();
		expense.setId(1L);
		expense.setTitle("Groceries");
		expense.setDescription("Weekly groceries");
		expense.setCategory("Food");
		expense.setAmount(150.0);
		expense.setDate(LocalDate.of(2023, 1, 15));
		expense.setUserId(1L);

		expenseDTO = new ExpenseDTO();
		expenseDTO.setTitle("Groceries");
		expenseDTO.setDescription("Weekly groceries");
		expenseDTO.setCategory("Food");
		expenseDTO.setAmount(150.0);
		expenseDTO.setDate(LocalDate.of(2023, 1, 15));
		expenseDTO.setUserId(1L);
	}

	@Test
	public void testDeleteExpense() {
		when(expenseRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(expense));

		String result = expenseService.deleteExpense(1L, 1L);

		assertEquals("Expense Deleted Successfully", result);
		verify(expenseRepository, times(1)).delete(expense);
	}

	@Test
	public void testDeleteExpense_NotFound() {
		when(expenseRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(ExpenseNotFoundException.class, () -> {
			expenseService.deleteExpense(1L, 1L);
		});
	}

	@Test
	public void testGetExpense() {
		when(expenseRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(expense));

		Expense foundExpense = expenseService.getExpense(1L, 1L);

		assertNotNull(foundExpense);
		assertEquals(expense.getTitle(), foundExpense.getTitle());
	}

	@Test
	public void testGetExpense_NotFound() {
		when(expenseRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(ExpenseNotFoundException.class, () -> {
			expenseService.getExpense(1L, 1L);
		});
	}

	@Test
	public void testGetAllExpenses() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("date")));
		Page<Expense> page = new PageImpl<>(Arrays.asList(expense));

		when(expenseRepository.findByUserId(eq(1L), any(Pageable.class))).thenReturn(page);

		Page<Expense> expenses = expenseService.getAllExpenses(1L, pageable);

		assertNotNull(expenses);
		assertEquals(1, expenses.getTotalElements());
	}

	@Test
	public void testGetTotalExpenses() {
		when(expenseRepository.sumAmountByUserId(1L)).thenReturn(150.0);

		Double totalExpenses = expenseService.getTotalExpenses(1L);

		assertEquals(150.0, totalExpenses);
	}

	@Test
	public void testGetTotalExpenses_NotFound() {
		when(expenseRepository.sumAmountByUserId(1L)).thenReturn(null);

		assertThrows(ExpenseNotFoundException.class, () -> {
			expenseService.getTotalExpenses(1L);
		});
	}

	@Test
	public void testAddExpense() {
		when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

		Expense result = expenseService.addExpense(expenseDTO);

		assertNotNull(result);
		assertEquals(expense.getTitle(), result.getTitle());
		assertEquals(expense.getDescription(), result.getDescription());
		assertEquals(expense.getCategory(), result.getCategory());
		assertEquals(expense.getAmount(), result.getAmount());
		assertEquals(expense.getDate(), result.getDate());
		assertEquals(expense.getUserId(), result.getUserId());
	}

	@Test
	public void testUpdateExpense() {
		when(expenseRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(expense));
		when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

		Expense updatedExpense = expenseService.updateExpense(1L, expenseDTO);

		assertNotNull(updatedExpense);
		assertEquals(expenseDTO.getTitle(), updatedExpense.getTitle());
	}

	@Test
	public void testUpdateExpense_NotFound() {
		when(expenseRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(ExpenseNotFoundException.class, () -> {
			expenseService.updateExpense(1L, expenseDTO);
		});
	}

	@Test
	public void testGetExpenseByUserIdAndDateBetween() {
		LocalDate startDate = LocalDate.of(2023, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 1, 31);
		when(expenseRepository.findByUserIdAndDateBetween(1L, startDate, endDate)).thenReturn(Arrays.asList(expense));

		List<Expense> expenses = expenseService.getExpenseByUserIdAndDateBetween(1L, startDate, endDate);

		assertNotNull(expenses);
		assertFalse(expenses.isEmpty());
	}

	@Test
	public void testGetExpenseByUserIdAndDateBetween_NotFound() {
		LocalDate startDate = LocalDate.of(2023, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 1, 31);
		when(expenseRepository.findByUserIdAndDateBetween(1L, startDate, endDate)).thenReturn(Arrays.asList());

		assertThrows(ExpenseNotFoundException.class, () -> {
			expenseService.getExpenseByUserIdAndDateBetween(1L, startDate, endDate);
		});
	}
}
