package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.StateType;
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

  @GetMapping("/state")
  public StateType getState(@RequestParam("machineName") String machineName) {
    return machineTwinService.getState(machineName);
  }

  @GetMapping("/ratios")
  public Collection<RatioDTO> getRatios(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getRatios(machineName, period);
  }

  @GetMapping("/materials-usage")
  public Collection<MaterialDTO> getMostUsedMaterials(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getMostUsedMaterials(machineName, period);
  }

  @GetMapping("/working-hours")
  public WorkingHoursDTO getWorkingHours(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getWorkingHours(machineName, period);
  }

  @GetMapping("/tools-info")
  public ToolsInfoDTO getToolsInfo(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getToolsInfo(machineName, period);
  }

  @GetMapping("/processes-info")
  public ProcessesInfoDTO getProcessesInfo(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getProcessesInfo(machineName, period);
  }

  @GetMapping("/breakdowns")
  public Collection<BreakdownDTO> getBreakdownsOccurred(
      @RequestParam("machineName") String machineName,
      @RequestParam(name = "period") Period period) {

    return machineTwinService.getBreakdownsOccurred(machineName, period);
  }
}
