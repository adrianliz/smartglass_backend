package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.repositories.MachineEventRepository;
import com.turomas.smartglass.twins.domain.dto.*;

import java.util.*;

public class MachineTwin {
  public enum TwinState {
    AVAILABLE,
    DOING_PROCESS,
    BREAKDOWN
  }

  private final String name;
  private final MachineEventRepository machineEventRepository;
  private final SortedSet<MachineProcess> processesPerformed;
  private final Map<DateRange, MachineRatios> ratios;
  private final SortedSet<MachineEvent> eventsOccurred;
  private TwinState state;

  public MachineTwin(String name, MachineEventRepository machineEventRepository) {
    this.name = name;
    this.machineEventRepository = machineEventRepository;
    state = TwinState.BREAKDOWN;
    ratios = new HashMap<>();
    processesPerformed = new TreeSet<>();
    eventsOccurred = new TreeSet<>();

    consumeEvents();
  }

  // TODO each x seg
  private void consumeEvents() {
    eventsOccurred.addAll(machineEventRepository.getMachineEvents(name));

    for (MachineEvent event : eventsOccurred) {
      MachineProcess lastProcessPerformed = null;

      if (!processesPerformed.isEmpty()) {
        lastProcessPerformed = processesPerformed.last();
      }

      if (lastProcessPerformed != null && lastProcessPerformed.inProgress()) {
        lastProcessPerformed.update(event);
      } else if (event.machineStartsProcess()) {
        processesPerformed.add(new MachineProcess(event));
      }
    }

    // updateState();
  }

  private void updateState(MachineEvent event) {
    if (event != null) {
      // TODO think
    }
  }

  public List<RatioDTO> calculateRatios(DateRange dateRange) {
    MachineRatios ratios = this.ratios.get(dateRange);

    if (ratios == null) {
      ratios = new MachineRatios(dateRange);
      this.ratios.put(dateRange, ratios);
    }

    return ratios.calculate(eventsOccurred, processesPerformed);
  }

  public WorkingStatisticsDTO calculateWorkingStatistics(DateRange dateRange) {
    return null;
  }

  public List<MaterialDTO> getMostUsedMaterials(DateRange dateRange) {
    return machineEventRepository.getMostUsedMaterials(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public List<OptimizationDTO> getOptimizationsHistory(DateRange dateRange) {
    return machineEventRepository.getOptimizationHistory(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public ToolInfoDTO getToolInfo(DateRange dateRange) {
    return machineEventRepository.getToolInfo(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public WheelInfoDTO getWheelInfo(DateRange dateRange) {
    return machineEventRepository.getWheelInfo(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public List<BreakdownDTO> getBreakdownsOccurred(DateRange dateRange) {
    return machineEventRepository.getBreakdownsOccurred(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }
}
