package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.services.MachineTwinService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/twins")
public class MachineTwinController {
  private final MachineTwinService machineTwinService;

  public MachineTwinController(MachineTwinService machineTwinService) {
    this.machineTwinService = machineTwinService;
  }

  @GetMapping("/ratios")
  public Collection<RatioDTO> getRatios(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getRatios(machineName, period);
  }

  @GetMapping("/statistics/materials-usage")
  public Collection<MaterialDTO> getMostUsedMaterials(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getMostUsedMaterials(machineName, period);
  }

  @GetMapping("/statistics/working")
  public WorkingStatisticsDTO getWorkingStatistics(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getWorkingStatistics(machineName, period);
  }

  @GetMapping("/statistics/optimizations-history")
  public Collection<OptimizationDTO> getOptimizationsHistory(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getOptimizationsHistory(machineName, period);
  }

  @GetMapping("/statistics/tool")
  public ToolInfoDTO getToolInfo(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getToolInfo(machineName, period);
  }

  @GetMapping("/statistics/wheel")
  public WheelInfoDTO getWheelInfo(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getWheelInfo(machineName, period);
  }

  @GetMapping("/statistics/breakdowns")
  public Collection<BreakdownDTO> getBreakdownsOccurred(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getBreakdownsOccurred(machineName, period);
  }
}
