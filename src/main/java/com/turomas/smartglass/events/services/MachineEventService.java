package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.MachineEvent;

import java.time.LocalDateTime;
import java.util.SortedSet;

public interface MachineEventService {
  SortedSet<MachineEvent> getMachineEvents(String machineName);

  SortedSet<MachineEvent> getNextMachineEvents(String machineName, LocalDateTime startDate);
}
