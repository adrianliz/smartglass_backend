package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.services.TwinsService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/twin/{twinName}")
public class TwinController {
  private final TwinsService twinsService;

  public TwinController(TwinsService twinsService) {
    this.twinsService = twinsService;
  }

  @GetMapping("/ratios")
  public Collection<RatioDTO> getRatios(
    @PathVariable String twinName, @RequestParam(name = "period") Period period) {

    return twinsService.getRatios(twinName, period);
  }

  @GetMapping("/materials-usage")
  public Collection<MaterialDTO> getMostUsedMaterials(
    @PathVariable String twinName, @RequestParam(name = "period") Period period) {

    return twinsService.getMostUsedMaterials(twinName, period);
  }

  @GetMapping("/optimizations")
  public Collection<OptimizationDTO> getOptimizations(
    @PathVariable String twinName, @RequestParam(name = "period") Period period) {

    return twinsService.getOptimizationsProcessed(twinName, period);
  }

  @GetMapping("/tools-info")
  public ToolsInfoDTO getToolsInfo(
    @PathVariable String twinName, @RequestParam(name = "period") Period period) {

    return twinsService.getToolsInfo(twinName, period);
  }

  @GetMapping("/usage-time")
  public UsageTimeDTO getUsageTime(
    @PathVariable String twinName, @RequestParam(name = "period") Period period) {

    return twinsService.getUsageTime(twinName, period);
  }

  @GetMapping("/breakdowns")
  public Collection<BreakdownDTO> getBreakdownsOccurred(
    @PathVariable String twinName, @RequestParam(name = "period") Period period) {

    return twinsService.getBreakdownsOccurred(twinName, period);
  }
}
