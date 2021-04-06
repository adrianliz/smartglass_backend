package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.DateRange;
import com.turomas.smartglass.twins.domain.dtos.statistics.*;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StatisticsProxy implements StatisticsService {
  private final TwinsService twinsService;

  public StatisticsProxy(TwinsService twinsService) {
    this.twinsService = twinsService;
  }

  @Override
  public Collection<RatioDTO> getRatios(String twinName, DateRange dateRange) throws TwinNotFound {
    return twinsService.getTwin(twinName).getRatios(dateRange);
  }

  @Override
  public Collection<MaterialDTO> getMaterialsUsed(String twinName, DateRange dateRange) throws TwinNotFound {
    return twinsService.getTwin(twinName).getMaterialsUsed(dateRange);
  }

  public MachineUsageDTO getMachineUsage(String twinName, DateRange dateRange) throws TwinNotFound {
    return twinsService.getTwin(twinName).getMachineUsage(dateRange);
  }

  @Override
  public Collection<OptimizationDTO> getOptimizationsProcessed(String twinName,
                                                               DateRange dateRange) throws TwinNotFound {
    return twinsService.getTwin(twinName).getOptimizationsProcessed(dateRange);
  }

  @Override
  public ToolsDTO getToolsInfo(String twinName, DateRange dateRange) throws TwinNotFound {
    return twinsService.getTwin(twinName).getToolsInfo(dateRange);
  }

  @Override
  public TimeDistributionDTO getTimeDistribution(String twinName, DateRange dateRange) throws TwinNotFound {
    return twinsService.getTwin(twinName).getTimeDistribution(dateRange);
  }

  @Override
  public Collection<ErrorDTO> getErrorsProduced(String twinName, DateRange dateRange) throws TwinNotFound {
    return twinsService.getTwin(twinName).getErrorsProduced(dateRange);
  }
}
