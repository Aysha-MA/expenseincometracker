package com.project.income;

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

import com.project.income.dto.IncomeDTO;
import com.project.income.entity.Income;
import com.project.income.exception.IncomeNotFoundException;
import com.project.income.repository.IncomeRepository;
import com.project.income.service.IncomeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class IncomeMicroserviceApplicationTests {

	@Mock
	private IncomeRepository incomeRepository;

	@InjectMocks
	private IncomeServiceImpl incomeService;

	private Income income;
	private IncomeDTO incomeDTO;

	@BeforeEach
	public void setUp() {
		income = new Income();
		income.setId(1L);
		income.setTitle("Salary");
		income.setDescription("Monthly salary");
		income.setCategory("Job");
		income.setAmount(5000.0);
		income.setDate(LocalDate.of(2023, 1, 15));
		income.setUserId(1L);

		incomeDTO = new IncomeDTO();
		incomeDTO.setTitle("Salary");
		incomeDTO.setDescription("Monthly salary");
		incomeDTO.setCategory("Job");
		incomeDTO.setAmount(5000.0);
		incomeDTO.setDate(LocalDate.of(2023, 1, 15));
		incomeDTO.setUserId(1L);
	}

	@Test
	public void testDeleteIncome() {
		when(incomeRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(income));

		String result = incomeService.deleteIncome(1L, 1L);

		assertEquals("Income Deleted Successfully", result);
		verify(incomeRepository, times(1)).delete(income);
	}

	@Test
	public void testDeleteIncome_NotFound() {
		when(incomeRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(IncomeNotFoundException.class, () -> {
			incomeService.deleteIncome(1L, 1L);
		});
	}

	@Test
	public void testGetIncome() {
		when(incomeRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(income));

		Income foundIncome = incomeService.getIncome(1L, 1L);

		assertNotNull(foundIncome);
		assertEquals(income.getTitle(), foundIncome.getTitle());
	}

	@Test
	public void testGetIncome_NotFound() {
		when(incomeRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(IncomeNotFoundException.class, () -> {
			incomeService.getIncome(1L, 1L);
		});
	}

	@Test
	public void testGetAllIncome() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("date")));
		Page<Income> page = new PageImpl<>(Arrays.asList(income));

		when(incomeRepository.findByUserId(eq(1L), any(Pageable.class))).thenReturn(page);

		Page<Income> incomes = incomeService.getAllIncome(1L, pageable);

		assertNotNull(incomes);
		assertEquals(1, incomes.getTotalElements());
	}

	@Test
	public void testGetTotalIncome() {
		when(incomeRepository.sumAmountByUserId(1L)).thenReturn(5000.0);

		Double totalIncome = incomeService.getTotalIncome(1L);

		assertEquals(5000.0, totalIncome);
	}

	@Test
	public void testGetTotalIncome_NotFound() {
		when(incomeRepository.sumAmountByUserId(1L)).thenReturn(null);

		assertThrows(IncomeNotFoundException.class, () -> {
			incomeService.getTotalIncome(1L);
		});
	}

	@Test
	public void testAddIncome() {
		when(incomeRepository.save(any(Income.class))).thenReturn(income);

		Income result = incomeService.addIncome(incomeDTO);

		assertNotNull(result);
		assertEquals(income.getTitle(), result.getTitle());
		assertEquals(income.getDescription(), result.getDescription());
		assertEquals(income.getCategory(), result.getCategory());
		assertEquals(income.getAmount(), result.getAmount());
		assertEquals(income.getDate(), result.getDate());
		assertEquals(income.getUserId(), result.getUserId());
	}

	@Test
	public void testUpdateIncome() {
		when(incomeRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(income));
		when(incomeRepository.save(any(Income.class))).thenReturn(income);

		Income updatedIncome = incomeService.updateIncome(1L, incomeDTO);

		assertNotNull(updatedIncome);
		assertEquals(incomeDTO.getTitle(), updatedIncome.getTitle());
	}

	@Test
	public void testUpdateIncome_NotFound() {
		when(incomeRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

		assertThrows(IncomeNotFoundException.class, () -> {
			incomeService.updateIncome(1L, incomeDTO);
		});
	}

	@Test
	public void testGetIncomesByUserIdAndDateBetween() {
		LocalDate startDate = LocalDate.of(2023, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 1, 31);
		when(incomeRepository.findByUserIdAndDateBetween(1L, startDate, endDate)).thenReturn(Arrays.asList(income));

		List<Income> incomes = incomeService.getIncomesByUserIdAndDateBetween(1L, startDate, endDate);

		assertNotNull(incomes);
		assertFalse(incomes.isEmpty());
	}

	@Test
	public void testGetIncomesByUserIdAndDateBetween_NotFound() {
		LocalDate startDate = LocalDate.of(2023, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 1, 31);
		when(incomeRepository.findByUserIdAndDateBetween(1L, startDate, endDate)).thenReturn(Arrays.asList());

		assertThrows(IncomeNotFoundException.class, () -> {
			incomeService.getIncomesByUserIdAndDateBetween(1L, startDate, endDate);
		});
	}
}
