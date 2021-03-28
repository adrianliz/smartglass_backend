package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.dtos.*;
import com.turomas.smartglass.twins.repositories.exceptions.TwinNotFound;

import java.util.Collection;

public interface StatisticsService {
	Collection<RatioDTO> getRatios(String twinName, Period period) throws TwinNotFound;

	Collection<MaterialDTO> getMostUsedMaterials(String twinName, Period period) throws TwinNotFound;

	MachineUsageDTO getMachineUsage(String twinName, Period period) throws TwinNotFound;

	Collection<OptimizationDTO> getOptimizationsProcessed(String twinName, Period period) throws TwinNotFound;

	ToolsInfoDTO getToolsInfo(String twinName, Period period) throws TwinNotFound;

	TimeDistributionDTO getTimeDistribution(String twinName, Period period) throws TwinNotFound;

	Collection<BreakdownDTO> getBreakdownsOccurred(String twinName, Period period) throws TwinNotFound;
}
