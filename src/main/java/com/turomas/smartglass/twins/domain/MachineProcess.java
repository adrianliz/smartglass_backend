package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.twins.domain.exceptions.InvalidMachineProcess;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalDateTime;

public class MachineProcess implements Comparable<MachineProcess> {
  public enum ProcessState {
    COMPLETED,
    IN_PROGRESS,
    ABORTED
  }

  private ProcessState state;
  @EqualsAndHashCode.Include private final MachineEvent startEvent;
  private MachineEvent endEvent;

  public MachineProcess(MachineEvent startEvent) throws InvalidMachineProcess {
    if (startEvent.machineStartsProcess()) {
      this.startEvent = startEvent; // TODO validate if is a start_process event
      state = ProcessState.IN_PROGRESS;
    } else {
      throw new InvalidMachineProcess(startEvent);
    }
  }

  public LocalDateTime getEndDate() {
    return endEvent.getTimestamp();
  }

  public void update(MachineEvent event) {
    if (inProgress()) {
      if (event.machineCompletesProcess(startEvent)) {
        state = ProcessState.COMPLETED;
        endEvent = event;
      } else if (event.machineIsInBreakdown()) {
        state = ProcessState.ABORTED;
        endEvent = event;
      }
    }
  }

  public long workingSeconds() {
    if (!inProgress()) {
      return Duration.between(startEvent.getTimestamp(), endEvent.getTimestamp()).getSeconds();
    }

    return 0;
  }

  public long wastedSeconds(MachineProcess previousProcess) {
    if (previousProcess != null
        && !previousProcess.inProgress()
        && !inProgress()
        && this.compareTo(previousProcess) > 0) {

      return Duration.between(previousProcess.endEvent.getTimestamp(), startEvent.getTimestamp())
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
      LocalDateTime startTimestamp = startEvent.getTimestamp();

      return ((startTimestamp.compareTo(dateRange.getStartDate()) > 0)
          && (startTimestamp.compareTo(dateRange.getEndDate()) < 0));
    }

    return false;
  }

  @Override
  public int compareTo(MachineProcess machineProcess) {
    return startEvent.getTimestamp().compareTo(machineProcess.startEvent.getTimestamp());
  }
}
