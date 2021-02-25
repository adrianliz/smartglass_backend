package com.turomas.smartglass.machineEvent.services;

import com.turomas.smartglass.machineEvent.domain.MachineEvent;
import com.turomas.smartglass.machineEvent.services.exceptions.MachineEventNotFound;

import java.time.LocalDateTime;
import java.util.SortedSet;

public interface MachineEventService {
  MachineEvent getMachineEvent(String eventId) throws MachineEventNotFound;

  SortedSet<MachineEvent> getMachineEvents();

  SortedSet<MachineEvent> searchBy(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);
}
