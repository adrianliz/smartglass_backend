package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.repositories.MachineEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.SortedSet;

@Service
public class MachineEventProxy implements MachineEventService {
  private final MachineEventRepository machineEventRepository;

  public MachineEventProxy(MachineEventRepository machineEventRepository) {
    this.machineEventRepository = machineEventRepository;
  }

  @Override
  public SortedSet<MachineEvent> getMachineEvents(String machineName) {
    return machineEventRepository.getMachineEvents(machineName);
  }

  @Override
  public SortedSet<MachineEvent> getNextMachineEvents(String machineName, LocalDateTime startDate) {
    return machineEventRepository.getNextMachineEvents(machineName, startDate);
  }
}
