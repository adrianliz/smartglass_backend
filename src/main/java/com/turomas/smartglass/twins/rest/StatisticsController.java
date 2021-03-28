package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.dtos.*;
import com.turomas.smartglass.twins.services.StatisticsService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("{twinName}")
public class StatisticsController {
	private final StatisticsService statisticsService;

	public StatisticsController(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@GetMapping("/ratios")
	public Collection<RatioDTO> getRatios(
		@PathVariable String twinName, @RequestParam(name = "period") Period period) {

		return statisticsService.getRatios(twinName, period);
	}

	@GetMapping("/materials")
	public Collection<MaterialDTO> getMostUsedMaterials(
		@PathVariable String twinName, @RequestParam(name = "period") Period period) {

		return statisticsService.getMostUsedMaterials(twinName, period);
	}

	@GetMapping("/machine-usage")
	public MachineUsageDTO getMachineUsage(
		@PathVariable String twinName, @RequestParam(name = "period") Period period) {

		return statisticsService.getMachineUsage(twinName, period);
	}

	@GetMapping("/optimizations")
	public Collection<OptimizationDTO> getOptimizations(
		@PathVariable String twinName, @RequestParam(name = "period") Period period) {

		return statisticsService.getOptimizationsProcessed(twinName, period);
	}

	@GetMapping("/tools")
	public ToolsInfoDTO getToolsInfo(
		@PathVariable String twinName, @RequestParam(name = "period") Period period) {

		return statisticsService.getToolsInfo(twinName, period);
	}

	@GetMapping("/time-distribution")
	public TimeDistributionDTO getTimeDistribution(
		@PathVariable String twinName, @RequestParam(name = "period") Period period) {

		return statisticsService.getTimeDistribution(twinName, period);
	}

	@GetMapping("/breakdowns")
	public Collection<BreakdownDTO> getBreakdownsOccurred(
		@PathVariable String twinName, @RequestParam(name = "period") Period period) {

		return statisticsService.getBreakdownsOccurred(twinName, period);
	}
}
