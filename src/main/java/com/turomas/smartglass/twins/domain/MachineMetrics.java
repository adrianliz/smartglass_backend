package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.dto.ProcessesInfoDTO;
import com.turomas.smartglass.twins.domain.dto.RatioDTO;
import com.turomas.smartglass.twins.domain.dto.WorkingHoursDTO;

import java.util.*;

import static com.turomas.smartglass.twins.domain.dto.RatioDTO.RatioType.*;

public class MachineMetrics {
  private final DateRange dateRange;
  private MachineState lastStateEvaluated;
  private final Map<ProcessName, Long> workingSeconds;
  private long standbySeconds;
  private long breakdownSeconds;
  private long completedProcesses;
  private long abortedProcesses;

  public MachineMetrics(DateRange dateRange) {
    this.dateRange = dateRange;
    workingSeconds = new HashMap<>();
    standbySeconds = 0;
    breakdownSeconds = 0;
    completedProcesses = 0;
    abortedProcesses = 0;
  }

  private void evaluateState(MachineState state) {
    if (state.doingProcess()) {
      ProcessName processName = state.getProcessName();
      Long previousSeconds;

      if ((previousSeconds = workingSeconds.get(processName)) != null) {
        workingSeconds.put(processName, previousSeconds + state.secondsInState());
      } else {
        workingSeconds.put(processName, state.secondsInState());
      }
    } else if (state.inStandby()) {
      standbySeconds += state.secondsInState();

      if (state.processWasFinished()) {
        completedProcesses++;
      }
    } else {
      breakdownSeconds += state.secondsInState();

      if (lastStateEvaluated != null && lastStateEvaluated.doingProcess()) {
        abortedProcesses++;
      }
    }

    lastStateEvaluated = state;
  }

  private void evaluateStates(Collection<MachineState> states) {
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

  public Collection<RatioDTO> calculateRatios(Collection<MachineState> states) {
    evaluateStates(Collections.unmodifiableCollection(states));
    long workingSeconds = this.workingSeconds.values().stream().mapToLong(Long::longValue).sum();

    return List.of(
        new RatioDTO(AVAILABILITY, (workingSeconds + standbySeconds), breakdownSeconds),
        new RatioDTO(EFFICIENCY, workingSeconds, standbySeconds),
        new RatioDTO(EFFECTIVENESS, completedProcesses, abortedProcesses));
  }

  public WorkingHoursDTO calculateWorkingHours(Collection<MachineState> states) {
    evaluateStates(Collections.unmodifiableCollection(states));
    long workingSeconds = this.workingSeconds.values().stream().mapToLong(Long::longValue).sum();

    return new WorkingHoursDTO(workingSeconds, (workingSeconds + standbySeconds));
  }

  public ProcessesInfoDTO calculateProcessesInfo(Collection<MachineState> states) {
    evaluateStates(Collections.unmodifiableCollection(states));
    long processingGlassSeconds =
        this.workingSeconds.get(ProcessName.CUT) + this.workingSeconds.get(ProcessName.LOWE);
    long loadingGlassSeconds = this.workingSeconds.get(ProcessName.LOAD_GLASS);

    return new ProcessesInfoDTO(processingGlassSeconds, loadingGlassSeconds, standbySeconds);
  }
}
