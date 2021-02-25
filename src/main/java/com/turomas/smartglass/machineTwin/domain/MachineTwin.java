package com.turomas.smartglass.machineTwin.domain;

import com.turomas.smartglass.machineEvent.repositories.MachineEventRepository;

import java.util.HashMap;
import java.util.Map;

public class MachineTwin {
  private final String name;
  private final Map<Period, MachineTwinRatios> ratiosByPeriod;

  public MachineTwin(String name) {
    this.name = name;
    ratiosByPeriod = new HashMap<>();
  }

  public RatioDTO calculateAvailability(
      MachineEventRepository machineEventRepository, Period period) {

    MachineTwinRatios machineTwinRatios = this.ratiosByPeriod.get(period);

    if (machineTwinRatios == null) {
      machineTwinRatios = new MachineTwinRatios(name, period);
      this.ratiosByPeriod.put(period, machineTwinRatios);
    }

    return machineTwinRatios.calculateAvailability(machineEventRepository);
  }
}
