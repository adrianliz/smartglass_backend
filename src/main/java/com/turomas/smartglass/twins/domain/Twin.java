package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dtos.statistics.*;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import lombok.NonNull;

import java.util.Collection;
import java.util.SortedSet;

public class Twin {
  @NonNull
  private final StatesMachine statesMachine;
  @NonNull
  private final StatesStatistics statesStatistics;
  @NonNull
  private final EventsStatistics eventsStatistics;

  public Twin(StatesMachine statesMachine, StatesStatistics statesStatistics, EventsStatistics eventsStatistics) {
    this.statesMachine = statesMachine;
    this.statesStatistics = statesStatistics;
    this.eventsStatistics = eventsStatistics;

  }

  public SortedSet<TwinState> processEvents(EventsService eventsService) {
    return statesMachine.processEvents(eventsService);
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
