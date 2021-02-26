package com.turomas.smartglass.machineTwin.domain;

import com.turomas.smartglass.machineEvent.repositories.MachineEventRepository;
import com.turomas.smartglass.machineTwin.domain.exceptions.InvalidRatio;

import java.util.HashMap;
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

  public RatioDTO calculateRatio(
      RatioType ratio, Period period, MachineEventRepository machineEventRepository)
      throws InvalidRatio {

    return getRatiosByPeriod(period).calculateRatio(ratio, machineEventRepository);
  }
}
