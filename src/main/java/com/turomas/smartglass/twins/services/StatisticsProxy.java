package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.dtos.*;
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
	public Collection<RatioDTO> getRatios(String twinName, Period period) throws TwinNotFound {
		return twinsService.getTwin(twinName).getRatios(period.getDateRange());
	}

	@Override
	public Collection<MaterialDTO> getMostUsedMaterials(String twinName, Period period) throws TwinNotFound {
		return null;
	}

	public MachineUsageDTO getMachineUsage(String twinName, Period period) throws TwinNotFound {
		return twinsService.getTwin(twinName).getMachineUsage(period.getDateRange());
	}

	@Override
	public Collection<OptimizationDTO> getOptimizationsProcessed(String twinName, Period period) throws TwinNotFound {
		return null;
	}

	@Override
	public ToolsInfoDTO getToolsInfo(String twinName, Period period) throws TwinNotFound {
		return null;
	}

	@Override
	public TimeDistributionDTO getTimeDistribution(String twinName, Period period) throws TwinNotFound {
		return twinsService.getTwin(twinName).getTimeDistribution(period.getDateRange());
	}

	@Override
	public Collection<BreakdownDTO> getBreakdownsOccurred(String twinName, Period period) throws TwinNotFound {
		return null;
	}
}
