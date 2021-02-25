package com.turomas.smartglass.machineTwin.services;

import com.turomas.smartglass.machineEvent.repositories.MachineEventRepository;
import com.turomas.smartglass.machineTwin.domain.MachineTwin;
import com.turomas.smartglass.machineTwin.domain.RatioDTO;
import com.turomas.smartglass.machineTwin.domain.Period;
import com.turomas.smartglass.machineTwin.repositories.MachineTwinRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MachineTwinTwinProxy implements MachineTwinService {
  private final MachineTwinRepository machineTwinRepository;
  private final MachineEventRepository machineEventRepository;

  public MachineTwinTwinProxy(
      MachineTwinRepository machineTwinRepository, MachineEventRepository machineEventRepository) {
    this.machineTwinRepository = machineTwinRepository;
    this.machineEventRepository = machineEventRepository;
  }

  @Override
  public RatioDTO getAvailability(
      String machineTwinName, LocalDateTime startDate, LocalDateTime endDate) {

    MachineTwin machineTwin = machineTwinRepository.getMachineTwin(machineTwinName);
    return machineTwin.calculateAvailability(
        machineEventRepository, new Period(startDate, endDate));
  }
}
