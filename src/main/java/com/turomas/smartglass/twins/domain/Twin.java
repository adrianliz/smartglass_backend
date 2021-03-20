package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dto.*;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.groupingBy;

public class Twin {
  private final TwinOntology representation;
  private final StatesMachine statesMachine;
  private final EventsService eventsService;
  private final List<TwinState> transitedStates;
  private final Map<DateRange, TwinMetrics> metrics;
  private Event lastEventEvaluated;

  public Twin(TwinOntology representation, StatesMachine statesMachine, EventsService eventsService) {
    this.representation = representation;
    this.statesMachine = statesMachine;
    this.eventsService = eventsService;
    transitedStates = Collections.synchronizedList(new ArrayList<>());
    metrics = Collections.synchronizedMap(new HashMap<>());

    evaluateEvents();
  }

  private SortedSet<Event> getNewEvents() {
    if (lastEventEvaluated != null) {
      return eventsService.getSubsequentEvents(representation.getTwinName(), lastEventEvaluated.getTimestamp());
    }

    return eventsService.getEvents(representation.getTwinName());
  }

  // TODO update each day
  private void evaluateEvents() {
    for (Event event : getNewEvents()) {
      TwinStateId newStateId = statesMachine.doTransition(event);

      if (transitedStates.isEmpty()) {
        transitedStates.add(new TwinState(newStateId, event));
      } else {
        TwinState lastState = transitedStates.get(transitedStates.size() - 1);
        lastState.update(newStateId, event, transitedStates);
      }
      lastEventEvaluated = event;
    }
  }

  private TwinMetrics getMetrics(DateRange dateRange) {
    TwinMetrics metrics = this.metrics.get(dateRange);

    if (metrics == null) {
      metrics = new TwinMetrics(dateRange);
      this.metrics.put(dateRange, metrics);
    }

    return metrics;
  }

  public TwinOntology getRepresentation() {
    representation.updateState(statesMachine.getCurrentState());
    return representation;
  }

  public Collection<RatioDTO> getRatios(DateRange dateRange) {
    return getMetrics(dateRange).calculateRatios(transitedStates);
  }

  public UsageTimeDTO getUsageTime(DateRange dateRange) {
    return getMetrics(dateRange).calculateUsageTime(transitedStates);
  }

  public Collection<MaterialDTO> getMostUsedMaterials(DateRange dateRange) {
    SortedSet<MaterialDTO> mostUsedMaterials = new TreeSet<>(Collections.reverseOrder());

    transitedStates.stream()
      .filter(state -> (state.machineEntersBetween(dateRange) && state.processWasFinished(ProcessName.CUT)))
      .collect(groupingBy(TwinState::getProcessedMaterial))
      .forEach((material, matchingStates) -> mostUsedMaterials.add(new MaterialDTO(material, matchingStates.size())));

    return mostUsedMaterials;
  }

  public Collection<OptimizationDTO> getOptimizationsProcessed(DateRange dateRange) {
    SortedSet<OptimizationDTO> optimizations = new TreeSet<>(Collections.reverseOrder());

    transitedStates.stream()
      .filter(state -> (state.machineEntersBetween(dateRange) && state.processWasFinished(ProcessName.CUT)))
      .collect(groupingBy(TwinState::getProcessedOptimization, groupingBy(TwinState::getProcessedMaterial)))
      .forEach((optimization, group) ->
                 group.forEach((material, matchingStates) ->
                                 optimizations.add(new OptimizationDTO(optimization, material, matchingStates.size()))));

    return optimizations;
  }

  public ToolsInfoDTO getToolsInfo(DateRange dateRange) {
    AtomicLong distanceCovered = new AtomicLong();
    AtomicInteger cuttingAngle = new AtomicInteger();
    AtomicInteger wheelDiameter = new AtomicInteger();

    transitedStates.stream()
      .filter(state -> (state.machineEntersBetween(dateRange) && state.processWasFinished(ProcessName.CUT)))
      .reduce((first, second) -> second)
      .ifPresent(
        state -> {
          cuttingAngle.set(state.getToolAngle());
          distanceCovered.set(state.getToolDistanceCovered());
        });

    transitedStates.stream()
      .filter(state -> (state.machineEntersBetween(dateRange) && state.processWasFinished(ProcessName.LOWE)))
      .reduce((first, second) -> second)
      .ifPresent(state -> wheelDiameter.set(state.getWheelDiameter()));

    return new ToolsInfoDTO(distanceCovered.get(), cuttingAngle.get(), wheelDiameter.get());
  }

  public Collection<BreakdownDTO> getBreakdownsOccurred(DateRange dateRange) {
    SortedSet<BreakdownDTO> breakdownsRanking = new TreeSet<>(Collections.reverseOrder());

    transitedStates.stream()
      .filter(state -> (state.machineEntersBetween(dateRange) && state.machineFiresError()))
      .collect(groupingBy(TwinState::getErrorName))
      .forEach((error, matchingStates) -> breakdownsRanking.add(new BreakdownDTO(error, matchingStates.size())));

    return breakdownsRanking;
  }
}
