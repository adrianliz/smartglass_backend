package com.turomas.smartglass.machineTwin.services;

import com.turomas.smartglass.machineEvent.repositories.MachineEventRepository;
import com.turomas.smartglass.machineTwin.domain.MachineTwin;
import com.turomas.smartglass.machineTwin.domain.Period;
import com.turomas.smartglass.machineTwin.domain.RatioDTO;
import com.turomas.smartglass.machineTwin.domain.RatioType;
import com.turomas.smartglass.machineTwin.domain.exceptions.InvalidRatio;
import com.turomas.smartglass.machineTwin.repositories.MachineTwinRepository;
import com.turomas.smartglass.machineTwin.services.exceptions.MachineTwinNotFound;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MachineTwinProxy implements MachineTwinService {
  private final MachineTwinRepository machineTwinRepository;
  private final MachineEventRepository machineEventRepository;

  public MachineTwinProxy(
      MachineTwinRepository machineTwinRepository, MachineEventRepository machineEventRepository) {
    this.machineTwinRepository = machineTwinRepository;
    this.machineEventRepository = machineEventRepository;
  }

  @Override
  public RatioDTO getRatio(
      String machineTwinName, RatioType ratio, LocalDateTime startDate, LocalDateTime endDate)
      throws MachineTwinNotFound, InvalidRatio {

    MachineTwin machineTwin = machineTwinRepository.getMachineTwin(machineTwinName);

    if (machineTwin != null) {
      return machineTwin.calculateRatio(
          ratio, new Period(startDate, endDate), machineEventRepository);
    }

    throw new MachineTwinNotFound(machineTwinName);
  }
}
