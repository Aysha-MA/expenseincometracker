package com.project.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.statistics.entity.Stats;

public interface StatsRepository extends JpaRepository<Stats, Long> {
	Stats findByUserId(Long userId);
}
