package com.project.statistics.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.statistics.clients.ExpenseClient;
import com.project.statistics.clients.IncomeClient;
import com.project.statistics.dto.ExpenseDTO;
import com.project.statistics.dto.GraphDTO;
import com.project.statistics.dto.IncomeDTO;
import com.project.statistics.dto.StatsDTO;
import com.project.statistics.entity.Stats;
import com.project.statistics.exception.NegativeBalanceException;
import com.project.statistics.repository.StatsRepository;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {

	private final ExpenseClient expenseClient;
    private final IncomeClient incomeClient;
    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(ExpenseClient expenseClient, IncomeClient incomeClient, StatsRepository statsRepository) {
        this.expenseClient = expenseClient;
        this.incomeClient = incomeClient;
        this.statsRepository = statsRepository;
    }

    @Override
    public GraphDTO getChartData(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        List<ExpenseDTO> expenseList;
        List<IncomeDTO> incomeList;

        try {
            expenseList = expenseClient.getExpenseByUserIdAndDateBetween(userId, startDate, endDate);
        } catch (Exception e) {
            expenseList = new ArrayList<>();
        }

        try {
            incomeList = incomeClient.getIncomesByUserIdAndDateBetween(userId, startDate, endDate);
        } catch (Exception e) {
            incomeList = new ArrayList<>();
        }

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setExpenseList(expenseList);
        graphDTO.setIncomeList(incomeList);

        return graphDTO;
    }

    @Override
    public StatsDTO getStats(Long userId) {
        Double totalIncome = incomeClient.getTotalIncome(userId);
        Double totalExpense = expenseClient.getTotalExpenses(userId);
        Double balance = totalIncome - totalExpense;

        if (balance < 0) {
            throw new NegativeBalanceException("The user’s balance has fallen below zero.");
        }

        Stats stats = statsRepository.findByUserId(userId);
        if (stats == null) {
            stats = new Stats();
            stats.setUserId(userId);
        }
        stats.setTotalIncome(totalIncome);
        stats.setTotalExpense(totalExpense);
        stats.setBalance(balance);
        statsRepository.save(stats);

        return new StatsDTO(stats.getTotalIncome(), stats.getTotalExpense(), stats.getBalance());
    }
}