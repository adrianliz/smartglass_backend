package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dtos.statistics.*;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;

import java.util.Collection;
import java.util.TreeSet;

public class Twin {
  private final String name;
  private final StatesStatistics statesStatistics;
  private final EventsStatistics eventsStatistics;
  private final StatesMachine statesMachine;

  public Twin(String name, StatesStatistics statesStatistics, EventsStatistics eventsStatistics,
              StatesMachine statesMachine) {

    this.name = name;
    this.statesStatistics = statesStatistics;
    this.eventsStatistics = eventsStatistics;
    this.statesMachine = statesMachine;
  }

  private Collection<Event> getNewEvents(EventsService eventsService) {
    Event lastEventEvaluated = statesMachine.getLastEventEvaluated();
    if (lastEventEvaluated != null) {
      return eventsService.getSubsequentEvents(name, lastEventEvaluated.getTimestamp());
    }

    return eventsService.getEvents(name);
  }

  public Collection<TwinState> processEvents(EventsService eventsService) {
    Collection<Event> eventsToProcess = getNewEvents(eventsService);
    Collection<TwinState> transitedStates = new TreeSet<>();

    if (! eventsToProcess.isEmpty()) {
      transitedStates = statesMachine.processEvents(eventsToProcess);
    }

    return transitedStates;
  }

  public TwinStateId getCurrentState() {
    return statesMachine.getCurrentState();
  }

  public Collection<RatioDTO> getRatios(DateRange dateRange) {
    return statesStatistics.calculateRatios(dateRange);
  }

  public MachineUsageDTO getMachineUsage(DateRange dateRange) {
    return statesStatistics.calculateMachineUsage(dateRange);
  }

  public TimeDistributionDTO getTimeDistribution(DateRange dateRange) {
    return statesStatistics.calculateTimeDistribution(dateRange);
  }

  public Collection<MaterialDTO> getMaterialsUsed(DateRange dateRange) {
    return eventsStatistics.calculateMaterialsUsage(dateRange);
  }

  public Collection<OptimizationDTO> getOptimizationsProcessed(DateRange dateRange) {
    return eventsStatistics.calculateOptimizationsProcessed(dateRange);
  }

  public ToolsDTO getToolsInfo(DateRange dateRange) {
    return eventsStatistics.calculateToolsInfo(dateRange);
  }

  public Collection<ErrorDTO> getErrorsProduced(DateRange dateRange) {
    return eventsStatistics.calculateErrorsProduced(dateRange);
  }
}
