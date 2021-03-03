package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.services.exceptions.MachineEventNotFound;

import java.time.LocalDateTime;
import java.util.SortedSet;

public interface MachineEventService {
  MachineEvent getMachineEvent(String eventId) throws MachineEventNotFound;

  SortedSet<MachineEvent> getMachineEvents(String machineName);

  SortedSet<MachineEvent> searchBy(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);
}
