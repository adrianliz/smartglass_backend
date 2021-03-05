package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.services.MachineEventService;
import com.turomas.smartglass.twins.domain.dto.*;

import java.util.*;
import java.util.stream.Collectors;

public class MachineTwin {
  public enum TwinState {
    AVAILABLE,
    DOING_PROCESS,
    BREAKDOWN
  }

  private final String name;
  private final MachineEventService machineEventService;
  private final SortedSet<MachineEvent> occurredEvents;
  private final SortedSet<MachineProcess> performedProcesses;
  private final Map<DateRange, MachineRatios> ratios;
  private MachineEvent lastEventEvaluated;
  private TwinState state;

  public MachineTwin(String name, MachineEventService machineEventService) {
    this.name = name;
    this.machineEventService = machineEventService;
    state = TwinState.BREAKDOWN;
    ratios = new HashMap<>();
    performedProcesses = new TreeSet<>();
    occurredEvents = new TreeSet<>();

    consumeEvents();
  }

  private Collection<MachineEvent> selectEventsToConsume() {
    if (lastEventEvaluated != null) {
      return occurredEvents.stream()
          .filter(event -> event.compareTo(lastEventEvaluated) > 0)
          .collect(Collectors.toCollection(TreeSet::new));
    }

    return Collections.unmodifiableCollection(occurredEvents);
  }

  // TODO consume each day
  private void consumeEvents() {
    occurredEvents.addAll(machineEventService.getMachineEvents(name));

    for (MachineEvent event : selectEventsToConsume()) {
      MachineProcess lastPerformedProcess = null;

      if (!performedProcesses.isEmpty()) {
        lastPerformedProcess = performedProcesses.last();
      }

      if (lastPerformedProcess != null && lastPerformedProcess.inProgress()) {
        lastPerformedProcess.update(event);
      } else if (event.machineStartsProcess()) {
        performedProcesses.add(new MachineProcess(event));
      }

      lastEventEvaluated = event;
    }

    updateState();
  }

  private void updateState() {
    if (!performedProcesses.isEmpty() && performedProcesses.last().inProgress()) {
      state = TwinState.DOING_PROCESS;
    } else if (lastEventEvaluated!= null && lastEventEvaluated.machineIsInBreakdown()) {
      state = TwinState.BREAKDOWN;
    } else {
      state = TwinState.AVAILABLE;
    }
  }

  public Collection<RatioDTO> calculateRatios(DateRange dateRange) {
    MachineRatios ratios = this.ratios.get(dateRange);

    if (ratios == null) {
      ratios = new MachineRatios(dateRange);
      this.ratios.put(dateRange, ratios);
    }

    return ratios.calculate(
        Collections.unmodifiableCollection(occurredEvents),
        Collections.unmodifiableCollection(performedProcesses));
  }

  public WorkingStatisticsDTO calculateWorkingStatistics(DateRange dateRange) {
    return null; // TODO
  }

  public Collection<MaterialDTO> getMostUsedMaterials(DateRange dateRange) {
    return machineEventService.getMostUsedMaterials(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public Collection<OptimizationDTO> getOptimizationsHistory(DateRange dateRange) {
    return machineEventService.getOptimizationHistory(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public ToolInfoDTO getToolInfo(DateRange dateRange) {
    return machineEventService.getToolInfo(name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public WheelInfoDTO getWheelInfo(DateRange dateRange) {
    return machineEventService.getWheelInfo(name, dateRange.getStartDate(), dateRange.getEndDate());
  }

  public Collection<BreakdownDTO> getBreakdownsOccurred(DateRange dateRange) {
    return machineEventService.getBreakdownsOccurred(
        name, dateRange.getStartDate(), dateRange.getEndDate());
  }
}
