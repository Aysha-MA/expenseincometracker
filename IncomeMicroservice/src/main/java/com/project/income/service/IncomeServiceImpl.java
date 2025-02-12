package com.project.income.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.income.dto.IncomeDTO;
import com.project.income.entity.Income;
import com.project.income.exception.IncomeNotFoundException;
import com.project.income.repository.IncomeRepository;

/**
 * Service implementation for managing incomes.
 */
@Service
@Transactional
public class IncomeServiceImpl implements IncomeService {

	private static final String INCOME_NOT_FOUND_MESSAGE = "Income Not Found";

	private final IncomeRepository incomeRepository;

	public IncomeServiceImpl(IncomeRepository incomeRepository) {
		this.incomeRepository = incomeRepository;
	}

	/**
	 * Deletes an income for a specific user by income ID. If the income is not
	 * found, an IncomeNotFoundException is thrown.
	 *
	 * @param id     the ID of the income to be deleted
	 * @param userId the ID of the user who owns the income
	 * @return a message indicating the result of the deletion
	 * @throws IncomeNotFoundException if the income is not found
	 */
	@Override
	public String deleteIncome(Long id, Long userId) {
		Optional<Income> optionalIncome = incomeRepository.findByIdAndUserId(id, userId);
		if (optionalIncome.isPresent()) {
			incomeRepository.delete(optionalIncome.get());
			return "Income Deleted Successfully";
		}
		throw new IncomeNotFoundException(INCOME_NOT_FOUND_MESSAGE);
	}

	/**
	 * Retrieves an income for a specific user by income ID. If the income is not
	 * found, an IncomeNotFoundException is thrown.
	 *
	 * @param id     the ID of the income to be retrieved
	 * @param userId the ID of the user who owns the income
	 * @return the retrieved income
	 * @throws IncomeNotFoundException if the income is not found
	 */
	@Override
	public Income getIncome(Long id, Long userId) {
		Optional<Income> optionalIncome = incomeRepository.findByIdAndUserId(id, userId);
		if (optionalIncome.isPresent()) {
			return optionalIncome.get();
		} else {
			throw new IncomeNotFoundException(INCOME_NOT_FOUND_MESSAGE);
		}
	}

	/**
	 * Retrieves a paginated list of all incomes for a specific user. If the
	 * pageable parameter is null, a default pageable is used.
	 *
	 * @param userId   the ID of the user whose incomes are to be retrieved
	 * @param pageable the pagination information
	 * @return a paginated list of incomes
	 */
	@Override
	public Page<Income> getAllIncome(Long userId, Pageable pageable) {
		if (pageable == null) {
			pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("date")));
		} else {
			pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
					Sort.by(Sort.Order.desc("date")));
		}
		return incomeRepository.findByUserId(userId, pageable);
	}

	/**
	 * Retrieves the total amount of incomes for a specific user. If no incomes are
	 * found, an IncomeNotFoundException is thrown.
	 *
	 * @param userId the ID of the user whose total incomes are to be retrieved
	 * @return the total amount of incomes
	 * @throws IncomeNotFoundException if no incomes are found
	 */
	@Override
	public Double getTotalIncome(Long userId) {
		Double totalIncome = incomeRepository.sumAmountByUserId(userId);
		if (totalIncome == null) {
			throw new IncomeNotFoundException("No income found for user ID: " + userId);
		}
		return totalIncome;
	}

	/**
	 * Adds a new income for a specific user.
	 *
	 * @param incomeDTO the data transfer object containing the income details
	 * @return the added income
	 */
	@Override
	public Income addIncome(IncomeDTO incomeDTO) {
		Income income = new Income();
		income.setTitle(incomeDTO.getTitle());
		income.setDescription(incomeDTO.getDescription());
		income.setCategory(incomeDTO.getCategory());
		income.setAmount(incomeDTO.getAmount());
		income.setDate(incomeDTO.getDate());
		income.setUserId(incomeDTO.getUserId());
		return incomeRepository.save(income);
	}

	/**
	 * Updates an existing income for a specific user. If the income is not found,
	 * an IncomeNotFoundException is thrown.
	 *
	 * @param id        the ID of the income to be updated
	 * @param incomeDTO the data transfer object containing the updated income
	 *                  details
	 * @return the updated income
	 * @throws IncomeNotFoundException if the income is not found
	 */
	@Override
	public Income updateIncome(Long id, IncomeDTO incomeDTO) {
		Optional<Income> optionalIncome = incomeRepository.findByIdAndUserId(id, incomeDTO.getUserId());
		if (optionalIncome.isPresent()) {
			Income income = optionalIncome.get();
			if (incomeDTO.getTitle() != null)
				income.setTitle(incomeDTO.getTitle());
			if (incomeDTO.getDescription() != null)
				income.setDescription(incomeDTO.getDescription());
			if (incomeDTO.getCategory() != null)
				income.setCategory(incomeDTO.getCategory());
			if (incomeDTO.getAmount() != null)
				income.setAmount(incomeDTO.getAmount());
			if (incomeDTO.getDate() != null)
				income.setDate(incomeDTO.getDate());
			return incomeRepository.save(income);
		}
		throw new IncomeNotFoundException(INCOME_NOT_FOUND_MESSAGE);
	}

	/**
	 * Retrieves a list of incomes for a specific user within a given date range. If
	 * no incomes are found, an IncomeNotFoundException is thrown.
	 *
	 * @param userId    the ID of the user whose incomes are to be retrieved
	 * @param startDate the start date of the date range
	 * @param endDate   the end date of the date range
	 * @return a list of incomes for the specified user and date range
	 * @throws IncomeNotFoundException if no incomes are found for the given date
	 *                                 range
	 */
	@Override
	public List<Income> getIncomesByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
		List<Income> incomes = incomeRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
		if (incomes.isEmpty()) {
			throw new IncomeNotFoundException("No incomes found for the given date range");
		}
		return incomes;
	}
}
