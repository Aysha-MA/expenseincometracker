package com.project.expense.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.expense.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	Optional<Expense> findByIdAndUserId(Long id, Long userId);

	void deleteByUserId(Long userId);

	@Query("SELECT SUM(e.amount) FROM Expense e WHERE e.userId = :userId")
	Double sumAmountByUserId(Long userId);

	Page<Expense> findByUserId(Long userId, Pageable pageable);

	List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

}
