package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.MachineTwin;
import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.repositories.MachineTwinRepository;
import com.turomas.smartglass.twins.services.exceptions.MachineTwinNotFound;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class MachineTwinProxy implements MachineTwinService {
  private final MachineTwinRepository machineTwinRepository;

  public MachineTwinProxy(MachineTwinRepository machineTwinRepository) {
    this.machineTwinRepository = machineTwinRepository;
  }

  private MachineTwin getMachineTwin(String machineName) throws MachineTwinNotFound {
    MachineTwin machineTwin = machineTwinRepository.getMachineTwin(machineName);

    if (machineTwin != null) return machineTwin;
    throw new MachineTwinNotFound(machineName);
  }

  @Override
  public Collection<RatioDTO> getRatios(String machineName, Period period) throws MachineTwinNotFound {
    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.calculateRatios(period.getPeriod());
  }

  @Override
  public Collection<MaterialDTO> getMostUsedMaterials(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getMostUsedMaterials(period.getPeriod());
  }

  @Override
  public WorkingStatisticsDTO getWorkingStatistics(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.calculateWorkingStatistics(period.getPeriod());
  }

  @Override
  public Collection<OptimizationDTO> getOptimizationsHistory(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getOptimizationsHistory(period.getPeriod());
  }

  @Override
  public ToolInfoDTO getToolInfo(String machineName, Period period) throws MachineTwinNotFound {
    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getToolInfo(period.getPeriod());
  }

  @Override
  public WheelInfoDTO getWheelInfo(String machineName, Period period) throws MachineTwinNotFound {
    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getWheelInfo(period.getPeriod());
  }

  @Override
  public Collection<BreakdownDTO> getBreakdownsOccurred(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getBreakdownsOccurred(period.getPeriod());
  }
}
