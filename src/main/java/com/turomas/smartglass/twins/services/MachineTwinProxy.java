package com.turomas.smartglass.twins.services;

import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.twins.domain.MachineTwin;
import com.turomas.smartglass.twins.domain.PeriodType;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.domain.exceptions.InvalidPeriod;
import com.turomas.smartglass.twins.repositories.MachineTwinRepository;
import com.turomas.smartglass.twins.services.exceptions.MachineTwinNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineTwinProxy implements MachineTwinService {
  private final MachineTwinRepository machineTwinRepository;
  private final MachineEventRepository machineEventRepository;

  public MachineTwinProxy(
      MachineTwinRepository machineTwinRepository, MachineEventRepository machineEventRepository) {
    this.machineTwinRepository = machineTwinRepository;
    this.machineEventRepository = machineEventRepository;
  }

  private MachineTwin getMachineTwin(String machineName) throws MachineTwinNotFound {
    MachineTwin machineTwin = machineTwinRepository.getMachineTwin(machineName);

    if (machineTwin != null) return machineTwin;
    throw new MachineTwinNotFound(machineName);
  }

  @Override
  public List<RatioDTO> getRatios(String machineName, PeriodType periodType)
      throws MachineTwinNotFound, InvalidPeriod {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.calculateRatios(periodType.getPeriod(), machineEventRepository);
  }

  @Override
  public List<MaterialDTO> getMostUsedMaterials(String machineName, PeriodType periodType)
      throws MachineTwinNotFound, InvalidPeriod {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getMostUsedMaterials(periodType.getPeriod(), machineEventRepository);
  }

  @Override
  public WorkingStatisticsDTO getWorkingStatistics(String machineName, PeriodType periodType)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.calculateWorkingStatistics(periodType.getPeriod(), machineEventRepository);
  }

  @Override
  public List<OptimizationDTO> getOptimizationsHistory(String machineName, PeriodType periodType)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getOptimizationsHistory(periodType.getPeriod(), machineEventRepository);
  }

  @Override
  public ToolInfoDTO getToolInfo(String machineName, PeriodType periodType)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getToolInfo(periodType.getPeriod(), machineEventRepository);
  }

  @Override
  public WheelInfoDTO getWheelInfo(String machineName, PeriodType periodType)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getWheelInfo(periodType.getPeriod(), machineEventRepository);
  }

  @Override
  public List<BreakdownDTO> getBreakdownsOccurred(String machineName, PeriodType periodType)
      throws MachineTwinNotFound {

    MachineTwin machineTwin = getMachineTwin(machineName);
    return machineTwin.getBreakdownsOccurred(periodType.getPeriod(), machineEventRepository);
  }
}
