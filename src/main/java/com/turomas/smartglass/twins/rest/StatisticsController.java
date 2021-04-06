package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.DateRange;
import com.turomas.smartglass.twins.domain.dtos.statistics.*;
import com.turomas.smartglass.twins.services.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("statistics/{twinName}")
public class StatisticsController {
  private final StatisticsService statisticsService;

  public StatisticsController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping("ratios")
  public Collection<RatioDTO> getRatios(@PathVariable String twinName,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

    return statisticsService.getRatios(twinName, new DateRange(startDate, endDate));
  }

  @GetMapping("materials-used")
  public Collection<MaterialDTO> getMaterialsUsed(
    @PathVariable String twinName,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

    return statisticsService.getMaterialsUsed(twinName, new DateRange(startDate, endDate));
  }

  @GetMapping("machine-usage")
  public MachineUsageDTO getMachineUsage(@PathVariable String twinName,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
    return statisticsService.getMachineUsage(twinName, new DateRange(startDate, endDate));
  }

  @GetMapping("optimizations-processed")
  public Collection<OptimizationDTO> getOptimizationsProcessed(
    @PathVariable String twinName,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

    return statisticsService.getOptimizationsProcessed(twinName, new DateRange(startDate, endDate));
  }

  @GetMapping("tools-info")
  public ToolsDTO getToolsInfo(@PathVariable String twinName,
                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

    return statisticsService.getToolsInfo(twinName, new DateRange(startDate, endDate));
  }

  @GetMapping("time-distribution")
  public TimeDistributionDTO getTimeDistribution(
    @PathVariable String twinName,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

    return statisticsService.getTimeDistribution(twinName, new DateRange(startDate, endDate));
  }

  @GetMapping("errors-produced")
  public Collection<ErrorDTO> getErrorsProduced(
    @PathVariable String twinName,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

    return statisticsService.getErrorsProduced(twinName, new DateRange(startDate, endDate));
  }
}
