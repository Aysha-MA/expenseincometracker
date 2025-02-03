package com.project.income.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.income.entity.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
	Optional<Income> findByIdAndUserId(Long id, Long userId);

	void deleteByUserId(Long userId);

	@Query("SELECT SUM(i.amount) FROM Income i WHERE i.userId = :userId")
	Double sumAmountByUserId(Long userId);

	Page<Income> findByUserId(Long userId, Pageable pageable);

	List<Income> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

}
