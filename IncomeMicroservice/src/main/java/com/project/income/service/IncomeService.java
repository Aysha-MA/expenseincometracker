package com.project.income.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.income.dto.IncomeDTO;
import com.project.income.entity.Income;

public interface IncomeService {
	public abstract Income addIncome(IncomeDTO incomeDTO);

	public abstract Income updateIncome(Long id, IncomeDTO incomeDTO);

	public abstract String deleteIncome(Long id, Long userId);

	public abstract Income getIncome(Long id, Long userId);

	public abstract Page<Income> getAllIncome(Long userId, Pageable pageable);

	public abstract Double getTotalIncome(Long userId);

	public abstract List<Income> getIncomesByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
