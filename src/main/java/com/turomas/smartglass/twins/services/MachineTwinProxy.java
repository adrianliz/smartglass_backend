package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.twins.domain.MachineTwin;
import com.turomas.smartglass.twins.domain.Period;
import com.turomas.smartglass.twins.domain.StateType;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.repositories.MachineTwinRepository;
import com.turomas.smartglass.twins.services.exceptions.MachineTwinNotFound;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
  public StateType getState(String machineName) throws MachineTwinNotFound {
    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getState();
  }

  @Override
  public Collection<RatioDTO> getRatios(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getRatios(period.getDateRange());
  }

  @Override
  public Collection<MaterialDTO> getMostUsedMaterials(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getMostUsedMaterials(period.getDateRange());
  }

  @Override
  public WorkingHoursDTO getWorkingHours(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getWorkingStatistics(period.getDateRange());
  }

  @Override
  public ToolsInfoDTO getToolsInfo(String machineName, Period period) throws MachineTwinNotFound {
    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getToolsInfo(period.getDateRange());
  }

  @Override
  public ProcessesInfoDTO getProcessesInfo(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getProcessesInfo(period.getDateRange());
  }

  @Override
  public Collection<BreakdownDTO> getBreakdownsOccurred(String machineName, Period period)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getBreakdownsOccurred(period.getDateRange());
  }
}
