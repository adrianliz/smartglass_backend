package com.turomas.smartglass.rest;

import com.turomas.smartglass.twins.domain.PeriodType;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.services.MachineTwinService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/twins")
public class MachineTwinController {
  private final MachineTwinService machineTwinService;

  public MachineTwinController(MachineTwinService machineTwinService) {
    this.machineTwinService = machineTwinService;
  }

  @GetMapping("/ratios")
  public List<RatioDTO> getRatios(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "periodType") PeriodType periodType) {

    return machineTwinService.getRatios(machineName, periodType);
  }

  @GetMapping("/statistics/materials-usage")
  public List<MaterialDTO> getMostUsedMaterials(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "periodType") PeriodType periodType) {

    return machineTwinService.getMostUsedMaterials(machineName, periodType);
  }

  @GetMapping("/statistics/working")
  public WorkingStatisticsDTO getWorkingStatistics(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "periodType") PeriodType periodType) {

    return machineTwinService.getWorkingStatistics(machineName, periodType);
  }

  @GetMapping("/statistics/optimizations-history")
  public List<OptimizationDTO> getOptimizationsHistory(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "periodType") PeriodType periodType) {

    return machineTwinService.getOptimizationsHistory(machineName, periodType);
  }

  @GetMapping("/statistics/tool")
  public ToolInfoDTO getToolInfo(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "periodType") PeriodType periodType) {

    return machineTwinService.getToolInfo(machineName, periodType);
  }

  @GetMapping("/statistics/wheel")
  public WheelInfoDTO getWheelInfo(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "periodType") PeriodType periodType) {

    return machineTwinService.getWheelInfo(machineName, periodType);
  }

  @GetMapping("/statistics/breakdowns")
  public List<BreakdownDTO> getBreakdownsOccurred(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "periodType") PeriodType periodType) {

    return machineTwinService.getBreakdownsOccurred(machineName, periodType);
  }
}
