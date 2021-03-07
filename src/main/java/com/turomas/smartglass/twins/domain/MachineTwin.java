package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.events.services.MachineEventService;
import com.turomas.smartglass.twins.domain.dto.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.groupingBy;

public class MachineTwin {
  private final String name;
  private final MachineEventService machineEventService;
  private final List<MachineState> states;
  private final Map<DateRange, MachineMetrics> metrics;
  private MachineEvent lastEventEvaluated;

  public MachineTwin(String name, MachineEventService machineEventService) {
    this.name = name;
    this.machineEventService = machineEventService;
    states = new ArrayList<>();
    metrics = new HashMap<>();

    consumeEvents();
  }

  private SortedSet<MachineEvent> produceEvents() {
    if (lastEventEvaluated != null) {
      return machineEventService.getNextMachineEvents(name, lastEventEvaluated.getTimestamp());
    }

    return machineEventService.getMachineEvents(name);
  }

  // TODO consume each day
  private void consumeEvents() {
    for (MachineEvent event : produceEvents()) {
      if (states.isEmpty()) {
        states.add(new MachineState(event));
      } else {
        MachineState lastState = states.get(states.size() - 1);
        lastState.update(event, states);
      }

      lastEventEvaluated = event;
    }
  }

  private MachineMetrics getMetrics(DateRange dateRange) {
    MachineMetrics metrics = this.metrics.get(dateRange);

    if (metrics == null) {
      metrics = new MachineMetrics(dateRange);
      this.metrics.put(dateRange, metrics);
    }

    return metrics;
  }

  public StateType getState() {
    return states.get(states.size() - 1).getType();
  }

  public Collection<RatioDTO> getRatios(DateRange dateRange) {
    return getMetrics(dateRange).calculateRatios(states);
  }

  public WorkingHoursDTO getWorkingStatistics(DateRange dateRange) {
    return getMetrics(dateRange).calculateWorkingHours(states);
  }

  public ProcessesInfoDTO getProcessesInfo(DateRange dateRange) {
    return getMetrics(dateRange).calculateProcessesInfo(states);
  }

  public Collection<MaterialDTO> getMostUsedMaterials(DateRange dateRange) {
    SortedSet<MaterialDTO> mostUsedMaterials = new TreeSet<>(Collections.reverseOrder());

    states.stream()
        .filter(
            state ->
                state.machineEntersBetween(dateRange) && state.processWasFinished(ProcessName.CUT))
        .collect(groupingBy(MachineState::getCuttingMaterial))
        .forEach(
            (material, matchingStates) ->
                mostUsedMaterials.add(new MaterialDTO(material, matchingStates.size())));

    return mostUsedMaterials;
  }

  public ToolsInfoDTO getToolsInfo(DateRange dateRange) {
    AtomicLong distanceCovered = new AtomicLong();
    AtomicInteger cuttingAngle = new AtomicInteger();
    AtomicInteger wheelDiameter = new AtomicInteger();

    states.stream()
        .filter(
            state ->
                state.machineEntersBetween(dateRange) && state.processWasFinished(ProcessName.CUT))
        .reduce((first, second) -> second)
        .ifPresent(
            state -> {
              cuttingAngle.set(state.getToolAngle());
              distanceCovered.set(state.getToolDistanceCovered());
            });

    states.stream()
        .filter(
            state ->
                state.machineEntersBetween(dateRange) && state.processWasFinished(ProcessName.LOWE))
        .reduce((first, second) -> second)
        .ifPresent(state -> wheelDiameter.set(state.getWheelDiameter()));

    return new ToolsInfoDTO(distanceCovered.get(), cuttingAngle.get(), wheelDiameter.get());
  }

  public Collection<BreakdownDTO> getBreakdownsOccurred(DateRange dateRange) {
    SortedSet<BreakdownDTO> breakdownsRanking = new TreeSet<>(Collections.reverseOrder());

    states.stream()
        .filter(state -> state.machineEntersBetween(dateRange) && state.inBreakdown())
        .collect(groupingBy(MachineState::getBreakdownName))
        .forEach(
            (error, matchingStates) ->
                breakdownsRanking.add(new BreakdownDTO(error, matchingStates.size())));

    return breakdownsRanking;
  }
}
