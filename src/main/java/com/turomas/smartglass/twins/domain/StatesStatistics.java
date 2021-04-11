package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.dtos.statistics.MachineUsageDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.RatioDTO;
import com.turomas.smartglass.twins.domain.dtos.statistics.TimeDistributionDTO;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import com.turomas.smartglass.twins.services.StatesService;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static com.turomas.smartglass.twins.domain.dtos.statistics.RatioDTO.RatioId.*;

public class StatesStatistics {
  @NonNull
  private final String twinName;
  @NonNull
  private final StatesService statesService;

  public StatesStatistics(String twinName, StatesService statesService) {
    this.twinName = twinName;
    this.statesService = statesService;
  }

  private long totalSecondsIf(Collection<TwinState> states, Predicate<TwinState> condition) {
    return states.stream().filter(condition).mapToLong(TwinState::durationSeconds).sum();
  }

  private long totalStatesThat(Collection<TwinState> states, Predicate<TwinState> condition) {
    return states.stream().filter(condition).count();
  }

  public Collection<RatioDTO> calculateRatios(DateRange dateRange) {
    long workingSeconds = 0, standbySeconds = 0, dontAvailableSeconds = 0, completedProcesses = 0, abortedProcesses = 0;

    if (dateRange != null) {
      Collection<TwinState> states = dateRange.getStatesInside(twinName, statesService);

      workingSeconds = totalSecondsIf(states, state -> state.typeIs(TwinStateType.DOING_PROCESS));
      standbySeconds = totalSecondsIf(states, state -> state.typeIs(TwinStateType.IN_STANDBY));
      dontAvailableSeconds = totalSecondsIf(states, state ->
        state.typeIs(TwinStateType.IN_BREAKDOWN) || state.typeIs(TwinStateType.OFF));
      completedProcesses = totalStatesThat(states, state ->
        state.typeIs(TwinStateType.DOING_PROCESS) && state.lastEventTypeIs(EventType.END_PROCESS));
      abortedProcesses = totalStatesThat(states, state ->
        state.typeIs(TwinStateType.DOING_PROCESS) && state.lastEventTypeIs(EventType.ERROR));
    }

    return List.of(
      new RatioDTO(AVAILABILITY, (workingSeconds + standbySeconds), dontAvailableSeconds),
      new RatioDTO(EFFICIENCY, workingSeconds, standbySeconds),
      new RatioDTO(EFFECTIVENESS, completedProcesses, abortedProcesses));
  }

  public MachineUsageDTO calculateMachineUsage(DateRange dateRange) {
    long workingSeconds = 0, onSeconds = 0;

    if (dateRange != null) {
      Collection<TwinState> states = dateRange.getStatesInside(twinName, statesService);

      workingSeconds = totalSecondsIf(states, state -> state.typeIs(TwinStateType.DOING_PROCESS));
      onSeconds = totalSecondsIf(states, state ->
        state.typeIs(TwinStateType.IN_STANDBY) || state.typeIs(TwinStateType.IN_BREAKDOWN) ||
        state.typeIs(TwinStateType.DOING_PROCESS));

    }

    return new MachineUsageDTO(workingSeconds, onSeconds);
  }

  public TimeDistributionDTO calculateTimeDistribution(DateRange dateRange) {
    long processingGlassSeconds = 0, loadingGlassSeconds = 0, standbySeconds = 0;

    if (dateRange != null) {
      Collection<TwinState> states = dateRange.getStatesInside(twinName, statesService);

      processingGlassSeconds = totalSecondsIf(states, state ->
        state.starts(ProcessName.CUT) || state.starts(ProcessName.LOWE));
      loadingGlassSeconds = totalSecondsIf(states, state -> state.starts(ProcessName.LOAD_GLASS));
      standbySeconds = totalSecondsIf(states, state -> state.typeIs(TwinStateType.IN_STANDBY));
    }

    return new TimeDistributionDTO(processingGlassSeconds, loadingGlassSeconds, standbySeconds);
  }
}
