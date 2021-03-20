package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.TwinOntology;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;

import java.util.Collection;

public interface TwinsService {
  Collection<TwinOntology> getTwins();

  TwinOntology getTwin(String twinName) throws TwinNotFound;

  Collection<RatioDTO> getRatios(String twinName, Period period) throws TwinNotFound;

  Collection<MaterialDTO> getMostUsedMaterials(String twinName, Period period) throws TwinNotFound;

  Collection<OptimizationDTO> getOptimizationsProcessed(String twinName, Period period) throws TwinNotFound;

  ToolsInfoDTO getToolsInfo(String twinName, Period period) throws TwinNotFound;

  UsageTimeDTO getUsageTime(String twinName, Period period) throws TwinNotFound;

  Collection<BreakdownDTO> getBreakdownsOccurred(String twinName, Period period) throws TwinNotFound;
}
