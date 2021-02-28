package com.turomas.smartglass.machineTwin.domain;

import com.turomas.smartglass.machineEvent.repositories.MachineEventRepository;

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
}
