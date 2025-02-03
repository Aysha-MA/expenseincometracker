package com.project.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.statistics.clients.ExpenseClient;
import com.project.statistics.clients.IncomeClient;
import com.project.statistics.dto.ExpenseDTO;
import com.project.statistics.dto.GraphDTO;
import com.project.statistics.dto.IncomeDTO;
import com.project.statistics.dto.StatsDTO;
import com.project.statistics.entity.Stats;
import com.project.statistics.exception.NegativeBalanceException;
import com.project.statistics.repository.StatsRepository;
import com.project.statistics.service.StatsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StatisticsMicroserviceApplicationTests {

	@Mock
	private ExpenseClient expenseClient;

	@Mock
	private IncomeClient incomeClient;

	@Mock
	private StatsRepository statsRepository;

	@InjectMocks
	private StatsServiceImpl statsService;

	private ExpenseDTO expenseDTO;
	private IncomeDTO incomeDTO;
	private Stats stats;

	@BeforeEach
	public void setUp() {
		expenseDTO = new ExpenseDTO();
		expenseDTO.setAmount(100.0);
		expenseDTO.setDate(LocalDate.of(2023, 1, 15));
		expenseDTO.setUserId(1L);

		incomeDTO = new IncomeDTO();
		incomeDTO.setAmount(5000.0);
		incomeDTO.setDate(LocalDate.of(2023, 1, 15));
		incomeDTO.setUserId(1L);

		stats = new Stats();
		stats.setUserId(1L);
		stats.setTotalIncome(5000.0);
		stats.setTotalExpense(100.0);
		stats.setBalance(4900.0);
	}

	@Test
	public void testGetChartData() {
		List<ExpenseDTO> expenseList = Arrays.asList(expenseDTO);
		List<IncomeDTO> incomeList = Arrays.asList(incomeDTO);

		when(expenseClient.getExpenseByUserIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(expenseList);
		when(incomeClient.getIncomesByUserIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(incomeList);

		GraphDTO graphDTO = statsService.getChartData(1L);

		assertNotNull(graphDTO);
		assertEquals(expenseList, graphDTO.getExpenseList());
		assertEquals(incomeList, graphDTO.getIncomeList());
	}

	@Test
	public void testGetStats() {
		when(incomeClient.getTotalIncome(1L)).thenReturn(5000.0);
		when(expenseClient.getTotalExpenses(1L)).thenReturn(100.0);
		when(statsRepository.findByUserId(1L)).thenReturn(stats);

		StatsDTO result = statsService.getStats(1L);

		assertNotNull(result);
		assertEquals(5000.0, result.getTotalIncome());
		assertEquals(100.0, result.getTotalExpense());
		assertEquals(4900.0, result.getBalance());
	}

	@Test
	public void testGetStats_NegativeBalance() {
		when(incomeClient.getTotalIncome(1L)).thenReturn(100.0);
		when(expenseClient.getTotalExpenses(1L)).thenReturn(5000.0);

		assertThrows(NegativeBalanceException.class, () -> {
			statsService.getStats(1L);
		});
	}
}
