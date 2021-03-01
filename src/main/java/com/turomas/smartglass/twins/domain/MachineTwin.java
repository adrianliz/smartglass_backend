package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.twins.domain.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineTwin {
  private final String name;
  private final Map<Period, MachineTwinRatios> ratiosByPeriod;

  public MachineTwin(String name) {
    this.name = name;
    ratiosByPeriod = new HashMap<>();
  }

  private MachineTwinRatios getRatiosByPeriod(Period period) {
    MachineTwinRatios machineTwinRatios = ratiosByPeriod.get(period);

    if (machineTwinRatios == null) {
      machineTwinRatios = new MachineTwinRatios(name, period);
      ratiosByPeriod.put(period, machineTwinRatios);
    }

    return machineTwinRatios;
  }

  public List<RatioDTO> calculateRatios(
      Period period, MachineEventRepository machineEventRepository) {

    return getRatiosByPeriod(period).calculateRatios(machineEventRepository);
  }

  public WorkingStatisticsDTO calculateWorkingStatistics(
      Period period, MachineEventRepository machineEventRepository) {

    return getRatiosByPeriod(period).calculateWorkingStatistics(machineEventRepository);
  }

  public List<MaterialDTO> getMostUsedMaterials(
      Period period, MachineEventRepository machineEventRepository) {

    return machineEventRepository.getMostUsedMaterials(
        name, period.getStartDate(), period.getEndDate());
  }

  public List<OptimizationDTO> getOptimizationsHistory(
      Period period, MachineEventRepository machineEventRepository) {

    return machineEventRepository.getOptimizationHistory(
        name, period.getStartDate(), period.getEndDate());
  }

  public ToolInfoDTO getToolInfo(Period period, MachineEventRepository machineEventRepository) {
    return machineEventRepository.getToolInfo(name, period.getStartDate(), period.getEndDate());
  }

  public WheelInfoDTO getWheelInfo(Period period, MachineEventRepository machineEventRepository) {
    return machineEventRepository.getWheelInfo(
        name, period.getStartDate(), period.getEndDate());
  }

  public List<BreakdownDTO> getBreakdownsOccurred(
      Period period, MachineEventRepository machineEventRepository) {
    return machineEventRepository.getBreakdownsOccurred(
        name, period.getStartDate(), period.getEndDate());
  }
}
