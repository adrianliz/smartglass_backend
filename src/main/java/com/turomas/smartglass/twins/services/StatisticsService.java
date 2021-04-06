package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.DateRange;
import com.turomas.smartglass.twins.domain.dtos.statistics.*;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;

import java.util.Collection;

public interface StatisticsService {
  Collection<RatioDTO> getRatios(String twinName, DateRange dateRange) throws TwinNotFound;

  Collection<MaterialDTO> getMaterialsUsed(String twinName, DateRange dateRange) throws TwinNotFound;

  MachineUsageDTO getMachineUsage(String twinName, DateRange dateRange) throws TwinNotFound;

  Collection<OptimizationDTO> getOptimizationsProcessed(String twinName, DateRange dateRange) throws TwinNotFound;

  ToolsDTO getToolsInfo(String twinName, DateRange dateRange) throws TwinNotFound;

  TimeDistributionDTO getTimeDistribution(String twinName, DateRange dateRange) throws TwinNotFound;

  Collection<ErrorDTO> getErrorsProduced(String twinName, DateRange dateRange) throws TwinNotFound;
}
