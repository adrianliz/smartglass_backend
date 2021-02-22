package com.turomas.smartglass.services;

import com.turomas.smartglass.domain.MachineEvent;
import com.turomas.smartglass.repositories.MachineEventRepository;
import com.turomas.smartglass.services.exceptions.MachineEventNotFound;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MachineEventProxy implements MachineEventService {
  private final MachineEventRepository machineEventRepository;

  public MachineEventProxy(MachineEventRepository machineEventRepository) {
    this.machineEventRepository = machineEventRepository;
  }

  @Override
  public List<MachineEvent> getMachineEvents() {
    return machineEventRepository.findAll();
  }

  @Override
  public MachineEvent getMachineEvent(@NonNull String eventId) throws MachineEventNotFound {
    return machineEventRepository
        .findById(eventId)
        .orElseThrow(() -> new MachineEventNotFound(eventId));
  }

  @Override
  public List<MachineEvent> searchBy(
      @NonNull String machineName,
      @NonNull LocalDateTime startDate,
      @NonNull LocalDateTime endDate) {

    return machineEventRepository.searchBy(machineName, startDate, endDate);
  }
}
