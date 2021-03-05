package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.twins.domain.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
  public List<MaterialDTO> getMostUsedMaterials(String machineName, LocalDateTime startDate, LocalDateTime endDate) {
    return machineEventRepository.getMostUsedMaterials(machineName, startDate, endDate);
  }

  @Override
  public List<OptimizationDTO> getOptimizationHistory(String machineName, LocalDateTime startDate, LocalDateTime endDate) {
    return machineEventRepository.getOptimizationHistory(machineName, startDate, endDate);
  }

  @Override
  public ToolInfoDTO getToolInfo(String machineName, LocalDateTime startDate, LocalDateTime endDate) {
    return machineEventRepository.getToolInfo(machineName, startDate, endDate);
  }

  @Override
  public WheelInfoDTO getWheelInfo(String machineName, LocalDateTime startDate, LocalDateTime endDate) {
    return machineEventRepository.getWheelInfo(machineName, startDate, endDate);
  }

  @Override
  public List<BreakdownDTO> getBreakdownsOccurred(String machineName, LocalDateTime startDate, LocalDateTime endDate) {
    return machineEventRepository.getBreakdownsOccurred(machineName, startDate, endDate);
  }
}
