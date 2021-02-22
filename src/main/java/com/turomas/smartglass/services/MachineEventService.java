package com.turomas.smartglass.services;

import com.turomas.smartglass.domain.MachineEvent;
import com.turomas.smartglass.services.exceptions.MachineEventNotFound;

import java.time.LocalDateTime;
import java.util.List;

public interface MachineEventService {
  MachineEvent getMachineEvent(String eventId) throws MachineEventNotFound;

  List<MachineEvent> getMachineEvents();

  List<MachineEvent> searchBy(String machineName, LocalDateTime startDate, LocalDateTime endDate);
}
