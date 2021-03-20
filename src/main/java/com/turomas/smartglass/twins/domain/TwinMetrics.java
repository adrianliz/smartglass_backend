package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.dto.RatioDTO;
import com.turomas.smartglass.twins.domain.dto.UsageTimeDTO;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;

import java.util.*;

import static com.turomas.smartglass.twins.domain.dto.RatioDTO.RatioId.*;

public class TwinMetrics {
  private final DateRange dateRange;
  private final Map<ProcessName, Long> workingSeconds;
  private final Map<TwinStateId, Long> dontWorkingSeconds;
  private TwinState lastStateEvaluated;
  private long completedProcesses;
  private long abortedProcesses;

  public TwinMetrics(DateRange dateRange) {
    this.dateRange = dateRange;
    workingSeconds = new HashMap<>();
    dontWorkingSeconds = new HashMap<>();
    completedProcesses = 0;
    abortedProcesses = 0;
  }

  private <T> void updateSeconds(Map<T, Long> seconds, T key, long newSeconds) {
    Long previousSeconds;

    if ((previousSeconds = seconds.get(key)) != null) {
      seconds.put(key, previousSeconds + newSeconds);
    } else {
      seconds.put(key, newSeconds);
    }
  }

  private void evaluateState(TwinState state) {
    long secondsInState = state.secondsInState();

    if (state.doingProcess()) {
      updateSeconds(workingSeconds, state.getProcessName(), secondsInState);
    } else {
      updateSeconds(dontWorkingSeconds, state.getId(), secondsInState);

      if (state.processWasFinished()) {
        completedProcesses++;
      } else if (state.processWasAborted()) {
        abortedProcesses++;
      }
    }

    lastStateEvaluated = state;
  }

  private synchronized void evaluateStates(Collection<TwinState> states) {
    if (lastStateEvaluated != null) {
      DateRange dateRange =
        new DateRange(lastStateEvaluated.getLastUpdate(), this.dateRange.getEndDate());

      states.stream()
        .filter(state -> state.machineEntersBetween(dateRange))
        .forEach(this::evaluateState);
    } else {
      states.stream()
        .filter(state -> state.machineEntersBetween(dateRange))
        .forEach(this::evaluateState);
    }
  }

  public Collection<RatioDTO> calculateRatios(Collection<TwinState> states) {
    evaluateStates(Collections.unmodifiableCollection(states));
    long workingSeconds = this.workingSeconds.values().stream().mapToLong(Long::longValue).sum();
    long standbySeconds = dontWorkingSeconds.getOrDefault(TwinStateId.IN_STANDBY, 0L);
    long breakdownSeconds = dontWorkingSeconds.getOrDefault(TwinStateId.IN_BREAKDOWN, 0L);
    long offSeconds = dontWorkingSeconds.getOrDefault(TwinStateId.OFF, 0L);

    return List.of(
      new RatioDTO(AVAILABILITY, (workingSeconds + standbySeconds), (breakdownSeconds + offSeconds)),
      new RatioDTO(EFFICIENCY, workingSeconds, standbySeconds),
      new RatioDTO(EFFECTIVENESS, completedProcesses, abortedProcesses));
  }

  public UsageTimeDTO calculateUsageTime(Collection<TwinState> states) {
    evaluateStates(Collections.unmodifiableCollection(states));
    long processingGlassSeconds =
      this.workingSeconds.getOrDefault(ProcessName.CUT, 0L)
      + this.workingSeconds.getOrDefault(ProcessName.LOWE, 0L)
      + this.workingSeconds.getOrDefault(ProcessName.TPF, 0L)
      + this.workingSeconds.getOrDefault(ProcessName.VINIL, 0L);

    long loadingGlassSeconds = this.workingSeconds.getOrDefault(ProcessName.LOAD_GLASS, 0L);
    long standbySeconds = dontWorkingSeconds.getOrDefault(TwinStateId.IN_STANDBY, 0L);
    long breakdownSeconds = dontWorkingSeconds.getOrDefault(TwinStateId.IN_BREAKDOWN, 0L);
    long offSeconds = dontWorkingSeconds.getOrDefault(TwinStateId.OFF, 0L);

    return new UsageTimeDTO(processingGlassSeconds, loadingGlassSeconds,
                            standbySeconds, breakdownSeconds, offSeconds);
  }
}
