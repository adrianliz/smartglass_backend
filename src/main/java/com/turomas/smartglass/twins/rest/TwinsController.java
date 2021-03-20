package com.turomas.smartglass.twins.rest;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.TwinOntology;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.services.TwinsService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/twins")
public class TwinsController {
  private final TwinsService twinsService;

  public TwinsController(TwinsService twinsService) {
    this.twinsService = twinsService;
  }

  @GetMapping("")
  public Collection<TwinOntology> getTwins() {
    return twinsService.getTwins();
  }

  @GetMapping("/{twinName}")
  public TwinOntology getTwin(@PathVariable String twinName) {
    return twinsService.getTwin(twinName);
  }

  @GetMapping("/ratios")
  public Collection<RatioDTO> getRatios(@RequestParam("twinName") String twinName,
                                        @RequestParam(name = "period") Period period) {

    return twinsService.getRatios(twinName, period);
  }

  @GetMapping("/materials-usage")
  public Collection<MaterialDTO> getMostUsedMaterials(@RequestParam("twinName") String twinName,
                                                      @RequestParam(name = "period") Period period) {

    return twinsService.getMostUsedMaterials(twinName, period);
  }

  @GetMapping("/optimizations")
  public Collection<OptimizationDTO> getOptimizations(@RequestParam("twinName") String twinName,
                                                      @RequestParam(name = "period") Period period) {

    return twinsService.getOptimizationsProcessed(twinName, period);
  }

  @GetMapping("/tools-info")
  public ToolsInfoDTO getToolsInfo(@RequestParam("twinName") String twinName,
                                   @RequestParam(name = "period") Period period) {

    return twinsService.getToolsInfo(twinName, period);
  }

  @GetMapping("/usage-time")
  public UsageTimeDTO getUsageTime(@RequestParam("twinName") String twinName,
                                   @RequestParam(name = "period") Period period) {

    return twinsService.getUsageTime(twinName, period);
  }

  @GetMapping("/breakdowns")
  public Collection<BreakdownDTO> getBreakdownsOccurred(@RequestParam("twinName") String twinName,
                                                        @RequestParam(name = "period") Period period) {

    return twinsService.getBreakdownsOccurred(twinName, period);
  }
}
