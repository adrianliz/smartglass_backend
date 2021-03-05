package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.twins.domain.exceptions.InvalidMachineProcess;

import java.time.Duration;
import java.time.LocalDateTime;

public class MachineProcess implements Comparable<MachineProcess> {
  public enum ProcessState {
    COMPLETED,
    IN_PROGRESS,
    ABORTED
  }

  private ProcessState state;
  private final MachineEvent startProcessEvent;
  private MachineEvent endProcessEvent;

  public MachineProcess(MachineEvent startProcessEvent) throws InvalidMachineProcess {
    if (startProcessEvent.machineStartsProcess()) {
      this.startProcessEvent = startProcessEvent;
      state = ProcessState.IN_PROGRESS;
    } else {
      throw new InvalidMachineProcess(startProcessEvent);
    }
  }

  public LocalDateTime getEndDate() {
    return endProcessEvent.getTimestamp();
  }

  public void update(MachineEvent event) {
    if (inProgress()) {
      if (event.machineCompletesProcess(startProcessEvent)) {
        state = ProcessState.COMPLETED;
        endProcessEvent = event;
      } else if (event.machineIsInBreakdown()) {
        state = ProcessState.ABORTED;
        endProcessEvent = event;
      }
    }
  }

  public long workingSeconds() {
    if (!inProgress()) {
      return Duration.between(startProcessEvent.getTimestamp(), endProcessEvent.getTimestamp())
          .getSeconds();
    }

    return 0; // Preguntar
  }

  public long wastedSeconds(MachineProcess previousProcess) {
    if (previousProcess != null
        && !previousProcess.inProgress()
        && this.compareTo(previousProcess) > 0) {

      return Duration.between(
              previousProcess.endProcessEvent.getTimestamp(), startProcessEvent.getTimestamp())
          .getSeconds();
    }

    return 0;
  }

  public boolean completed() {
    return state.equals(ProcessState.COMPLETED);
  }

  public boolean inProgress() {
    return state.equals(ProcessState.IN_PROGRESS);
  }

  public boolean aborted() {
    return state.equals(ProcessState.ABORTED);
  }

  public boolean startsBetween(DateRange dateRange) {
    if (dateRange != null) {
      LocalDateTime startTimestamp = startProcessEvent.getTimestamp();

      return ((startTimestamp.compareTo(dateRange.getStartDate()) > 0)
          && (startTimestamp.compareTo(dateRange.getEndDate()) < 0));
    }

    return false;
  }

  @Override
  public int compareTo(MachineProcess machineProcess) {
    return startProcessEvent.compareTo(machineProcess.startProcessEvent);
  }
}
