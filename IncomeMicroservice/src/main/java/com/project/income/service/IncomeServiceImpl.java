package com.project.income.service;

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

import com.project.income.dto.IncomeDTO;
import com.project.income.entity.Income;
import com.project.income.exception.IncomeNotFoundException;
import com.project.income.repository.IncomeRepository;

@Service
@Transactional
public class IncomeServiceImpl implements IncomeService {

	private static final String INCOME_NOT_FOUND_MESSAGE = "Income Not Found";

    private final IncomeRepository incomeRepository;

    @Autowired
    public IncomeServiceImpl(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @Override
    public String deleteIncome(Long id, Long userId) {
        Optional<Income> optionalIncome = incomeRepository.findByIdAndUserId(id, userId);
        if (optionalIncome.isPresent()) {
            incomeRepository.delete(optionalIncome.get());
            return "Income Deleted Successfully";
        }
        throw new IncomeNotFoundException(INCOME_NOT_FOUND_MESSAGE);
    }

    @Override
    public Income getIncome(Long id, Long userId) {
        return incomeRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IncomeNotFoundException(INCOME_NOT_FOUND_MESSAGE));
    }

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

    @Override
    public Double getTotalIncome(Long userId) {
        Double totalIncome = incomeRepository.sumAmountByUserId(userId);
        if (totalIncome == null) {
            throw new IncomeNotFoundException("No income found for user ID: " + userId);
        }
        return totalIncome;
    }

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

    @Override
    public List<Income> getIncomesByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Income> incomes = incomeRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        if (incomes.isEmpty()) {
            throw new IncomeNotFoundException("No incomes found for the given date range");
        }
        return incomes;
    }
}
