package com.project.statistics.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.statistics.dto.GraphDTO;
import com.project.statistics.dto.StatsDTO;
import com.project.statistics.service.StatsService;

/**
 * REST controller for managing statistics.
 */
@RestController
@RequestMapping("/statistics")
public class StatsController {

	private final StatsService statsService;

	public StatsController(StatsService statsService) {
		this.statsService = statsService;
	}

	/**
	 * Retrieves statistics for a specific user, including total income, total
	 * expenses, and balance.
	 *
	 * @param userId the ID of the user whose statistics are to be retrieved
	 * @return a StatsDTO containing the total income, total expenses, and balance
	 */
	@GetMapping("/stats")
	public StatsDTO getStats(@RequestParam Long userId) {
		return statsService.getStats(userId);
	}

	/**
	 * Retrieves chart data for a specific user, including expenses and incomes
	 * within the last 30 days.
	 *
	 * @param userId the ID of the user whose chart data is to be retrieved
	 * @return a GraphDTO containing lists of expenses and incomes
	 */
	@GetMapping("/chartdata")
	public GraphDTO getChartData(@RequestParam Long userId) {
		return statsService.getChartData(userId);
	}
}
