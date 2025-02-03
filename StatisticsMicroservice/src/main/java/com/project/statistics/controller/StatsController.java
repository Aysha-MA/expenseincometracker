package com.project.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.statistics.dto.GraphDTO;
import com.project.statistics.dto.StatsDTO;
import com.project.statistics.service.StatsService;

@RestController
@RequestMapping("/statistics")
public class StatsController {
	
	private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }
	@GetMapping("/stats")
	public StatsDTO getStats(@RequestParam Long userId) {
		return statsService.getStats(userId);
	}

	@GetMapping("/chartdata")
	public GraphDTO getChartData(@RequestParam Long userId) {
		return statsService.getChartData(userId);
	}
}
