package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.events.services.exceptions.MachineEventNotFound;
import lombok.NonNull;
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
  public SortedSet<MachineEvent> getMachineEvents() {
    return machineEventRepository.getMachineEvents();
  }

  @Override
  public MachineEvent getMachineEvent(@NonNull String eventId) throws MachineEventNotFound {
    return machineEventRepository
        .findById(eventId)
        .orElseThrow(() -> new MachineEventNotFound(eventId));
  }

  @Override
  public SortedSet<MachineEvent> searchBy(
      @NonNull String machineName,
      @NonNull LocalDateTime startDate,
      @NonNull LocalDateTime endDate) {

    return machineEventRepository.searchBy(machineName, startDate, endDate);
  }
}
