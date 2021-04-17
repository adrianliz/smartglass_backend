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
import java.util.stream.Stream;

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

  private Stream<TwinState> statesThat(Collection<TwinState> states, Predicate<TwinState> condition) {
    return states.stream().filter(condition);
  }

  private long overlapSecondsWith(Stream<TwinState> states, DateRange dateRange) {
    return states.mapToLong(state -> state.overlapSecondsWith(dateRange)).sum();
  }

  private long totalStatesThat(Collection<TwinState> states, Predicate<TwinState> condition) {
    return states.stream().filter(condition).count();
  }

  public Collection<RatioDTO> calculateRatios(DateRange dateRange) {
    long workingSeconds = 0, standbySeconds = 0, dontAvailableSeconds = 0, completedProcesses = 0, abortedProcesses = 0;

    if (dateRange != null) {
      Collection<TwinState> states = dateRange.getOverlapStates(twinName, statesService);

      workingSeconds = overlapSecondsWith(statesThat(states, state -> state.typeIs(TwinStateType.DOING_PROCESS)),
                                          dateRange);
      standbySeconds = overlapSecondsWith(statesThat(states, state -> state.typeIs(TwinStateType.IN_STANDBY)),
                                          dateRange);
      dontAvailableSeconds = overlapSecondsWith(statesThat(states, state -> state.typeIs(TwinStateType.IN_BREAKDOWN) ||
                                                                            state.typeIs(TwinStateType.OFF)),
                                                dateRange);
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
      Collection<TwinState> states = dateRange.getOverlapStates(twinName, statesService);

      workingSeconds = overlapSecondsWith(statesThat(states, state -> state.typeIs(TwinStateType.DOING_PROCESS)),
                                          dateRange);
      onSeconds = overlapSecondsWith(statesThat(states, (state -> state.typeIs(TwinStateType.IN_STANDBY) ||
                                                                  state.typeIs(TwinStateType.IN_BREAKDOWN) ||
                                                                  state.typeIs(TwinStateType.DOING_PROCESS))),
                                     dateRange);

    }

    return new MachineUsageDTO(workingSeconds, onSeconds);
  }

  public TimeDistributionDTO calculateTimeDistribution(DateRange dateRange) {
    long processingGlassSeconds = 0, loadingGlassSeconds = 0, standbySeconds = 0;

    if (dateRange != null) {
      Collection<TwinState> states = dateRange.getOverlapStates(twinName, statesService);

      processingGlassSeconds = overlapSecondsWith(statesThat(states, state -> state.starts(ProcessName.CUT) ||
                                                                              state.starts(ProcessName.LOWE)),
                                                  dateRange);
      loadingGlassSeconds = overlapSecondsWith(statesThat(states, state -> state.starts(ProcessName.LOAD_GLASS)),
                                               dateRange);
      standbySeconds = overlapSecondsWith(statesThat(states, state -> state.typeIs(TwinStateType.IN_STANDBY)),
                                          dateRange);
    }

    return new TimeDistributionDTO(processingGlassSeconds, loadingGlassSeconds, standbySeconds);
  }
}
