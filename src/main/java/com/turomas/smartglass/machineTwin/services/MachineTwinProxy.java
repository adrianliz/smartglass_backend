package com.turomas.smartglass.machineTwin.services;

import com.turomas.smartglass.machineEvent.domain.CuttingMaterial;
import com.turomas.smartglass.machineEvent.domain.Optimization;
import com.turomas.smartglass.machineEvent.repositories.MachineEventRepository;
import com.turomas.smartglass.machineTwin.domain.MachineTwin;
import com.turomas.smartglass.machineTwin.domain.Period;
import com.turomas.smartglass.machineTwin.domain.RatioDTO;
import com.turomas.smartglass.machineTwin.domain.exceptions.InvalidPeriod;
import com.turomas.smartglass.machineTwin.repositories.MachineTwinRepository;
import com.turomas.smartglass.machineTwin.services.exceptions.MachineTwinNotFound;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

  private MachineTwin getMachineTwin(String machineTwinName) throws MachineTwinNotFound {
    MachineTwin machineTwin = machineTwinRepository.getMachineTwin(machineTwinName);

    if (machineTwin != null) return machineTwin;
    throw new MachineTwinNotFound(machineTwinName);
  }

  @Override
  public List<RatioDTO> getRatios(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod {

    MachineTwin machineTwin = getMachineTwin(machineName);

    return machineTwin.calculateRatios(new Period(startDate, endDate), machineEventRepository);
  }

  @Override
  public List<CuttingMaterial> getMostUsedMaterials(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod {

    MachineTwin machineTwin = getMachineTwin(machineName);

    return machineTwin.getMostUsedMaterials(new Period(startDate, endDate), machineEventRepository);
  }

  @Override
  public List<Optimization> getOptimizationsHistory(
      String machineName, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidPeriod {

    MachineTwin machineTwin = getMachineTwin(machineName);

    return machineTwin.getOptimizationsHistory(
        new Period(startDate, endDate), machineEventRepository);
  }
}
