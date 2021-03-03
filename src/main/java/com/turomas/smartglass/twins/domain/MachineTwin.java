package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.twins.domain.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineTwin {
  private final String name;
  private final MachineEventRepository machineEventRepository;
  private final Map<DateRange, MachineTwinRatios> ratios;

  public MachineTwin(String name, MachineEventRepository machineEventRepository) {
    this.name = name;
    this.machineEventRepository = machineEventRepository;
    ratios = new HashMap<>();
  }

  private MachineTwinRatios getRatios(DateRange dateRange) {
    MachineTwinRatios machineTwinRatios = ratios.get(dateRange);

    if (machineTwinRatios == null) {
      machineTwinRatios = new MachineTwinRatios(name, dateRange);
      ratios.put(dateRange, machineTwinRatios);
    }

    return machineTwinRatios;
  }

  public List<RatioDTO> calculateRatios(DateRange dateRange) {
    return getRatios(dateRange).calculateRatios(machineEventRepository);
  }

  public WorkingStatisticsDTO calculateWorkingStatistics(DateRange dateRange) {
    return getRatios(dateRange).calculateWorkingStatistics(machineEventRepository);
  }

  public List<MaterialDTO> getMostUsedMaterials(DateRange dateRange) {
    return machineEventRepository.getMostUsedMaterials(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public List<OptimizationDTO> getOptimizationsHistory(DateRange dateRange) {
    return machineEventRepository.getOptimizationHistory(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public ToolInfoDTO getToolInfo(DateRange dateRange) {
    return machineEventRepository.getToolInfo(name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public WheelInfoDTO getWheelInfo(DateRange dateRange) {
    return machineEventRepository.getWheelInfo(name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public List<BreakdownDTO> getBreakdownsOccurred(DateRange dateRange) {
    return machineEventRepository.getBreakdownsOccurred(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }
}
