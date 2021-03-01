package com.turomas.smartglass.rest;

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
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineTwinService.getRatios(machineName, startDate, endDate);
  }

  @GetMapping("/statistics/materials-usage")
  public List<MaterialDTO> getMostUsedMaterials(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineTwinService.getMostUsedMaterials(machineName, startDate, endDate);
  }

  @GetMapping("/statistics/working")
  public WorkingStatisticsDTO getWorkingStatistics(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineTwinService.getWorkingStatistics(machineName, startDate, endDate);
  }

  @GetMapping("/statistics/optimizations-history")
  public List<OptimizationDTO> getOptimizationsHistory(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineTwinService.getOptimizationsHistory(machineName, startDate, endDate);
  }

  @GetMapping("/statistics/tool")
  public ToolInfoDTO getToolInfo(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineTwinService.getToolInfo(machineName, startDate, endDate);
  }

  @GetMapping("/statistics/wheel")
  public WheelInfoDTO getWheelInfo(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineTwinService.getWheelInfo(machineName, startDate, endDate);
  }

  @GetMapping("/statistics/breakdowns")
  public List<BreakdownDTO> getBreakdownsOccurred(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime startDate,
      @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime endDate) {

    return machineTwinService.getBreakdownsOccurred(machineName, startDate, endDate);
  }
}
