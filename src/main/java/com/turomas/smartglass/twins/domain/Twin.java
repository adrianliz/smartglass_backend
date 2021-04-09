package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.dtos.statistics.*;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;

import java.util.Collection;

public class Twin {
  private final StatesMachine statesMachine;
  private final StatesStatistics statesStatistics;
  private final EventsStatistics eventsStatistics;

  public Twin(StatesMachine statesMachine, StatesStatistics statesStatistics, EventsStatistics eventsStatistics) {
    this.statesStatistics = statesStatistics;
    this.eventsStatistics = eventsStatistics;
    this.statesMachine = statesMachine;
  }

  public Collection<TwinState> processEvents(EventsService eventsService) {
    return statesMachine.processEvents(eventsService);
  }

  public TwinStateType getCurrentState() {
    return statesMachine.getCurrentStateType();
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
