package com.turomas.smartglass.events.services;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.twins.domain.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;

public interface MachineEventService {
  SortedSet<MachineEvent> getMachineEvents(String machineName);

  List<MaterialDTO> getMostUsedMaterials(
    String machineName, LocalDateTime startDate, LocalDateTime endDate);

  List<OptimizationDTO> getOptimizationHistory(
    String machineName, LocalDateTime startDate, LocalDateTime endDate);

  ToolInfoDTO getToolInfo(String machineName, LocalDateTime startDate, LocalDateTime endDate);

  WheelInfoDTO getWheelInfo(String machineName, LocalDateTime startDate, LocalDateTime endDate);

  List<BreakdownDTO> getBreakdownsOccurred(
    String machineName, LocalDateTime startDate, LocalDateTime endDate);
}
