package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.exceptions.RequiredState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MachineState {
  private final StateType type;
  private final MachineEvent stateEnterEvent;
  private final MachineState previousState;
  private MachineEvent lastEventEvaluated;

  public MachineState(MachineEvent stateEnterEvent) {
    switch (stateEnterEvent.getType()) {
      case START_PROCESS:
        this.type = StateType.DOING_PROCESS;
        break;

      case ERROR:
        this.type = StateType.IN_BREAKDOWN;
        break;

      default:
        type = StateType.IN_STANDBY;
    }

    this.stateEnterEvent = stateEnterEvent;
    previousState = this;
    lastEventEvaluated = stateEnterEvent;
  }

  private MachineState(StateType type, MachineEvent stateEnterEvent, MachineState previousState) {
    this.type = type;
    this.stateEnterEvent = stateEnterEvent;
    this.previousState = previousState;
    lastEventEvaluated = stateEnterEvent;
  }

  public void update(MachineEvent event, List<MachineState> states) {
    StateType newType = type.doTransition(event, stateEnterEvent);

    if (!newType.equals(type)) {
      states.add(new MachineState(newType, event, this));
    }

    lastEventEvaluated = event;
  }

  public long secondsInState() {
    return Duration.between(stateEnterEvent.getTimestamp(), lastEventEvaluated.getTimestamp())
        .getSeconds();
  }

  public boolean machineEntersBetween(DateRange dateRange) {
    if (dateRange != null) {
      LocalDateTime enterDate = stateEnterEvent.getTimestamp();

      return (enterDate.isAfter(dateRange.getStartDate())
          && enterDate.isBefore(dateRange.getEndDate()));
    }

    return false;
  }

  public boolean doingProcess() {
    return type.equals(StateType.DOING_PROCESS);
  }

  public boolean inStandby() {
    return type.equals(StateType.IN_STANDBY);
  }

  public boolean inBreakdown() {
    return type.equals(StateType.IN_BREAKDOWN);
  }

  public boolean processWasFinished() {
    return (inStandby() && previousState.doingProcess());
  }

  public boolean processWasFinished(ProcessName processName) {
    return (processWasFinished() && previousState.getProcessName().equals(processName));
  }

  public StateType getType() {
    return type;
  }

  public LocalDateTime getLastUpdate() {
    return lastEventEvaluated.getTimestamp();
  }

  public ProcessName getProcessName() throws RequiredState {
    if (!doingProcess()) throw new RequiredState(StateType.DOING_PROCESS);
    return stateEnterEvent.getParams().getProcessName();
  }

  public String getBreakdownName() throws RequiredState {
    if (!inBreakdown()) throw new RequiredState(StateType.IN_BREAKDOWN);
    return stateEnterEvent.getErrorName();
  }

  public String getCuttingMaterial() throws RequiredState {
    if (!processWasFinished(ProcessName.CUT)) throw new RequiredState(ProcessName.CUT);
    return Objects.requireNonNullElse(stateEnterEvent.getParams().getMaterial(), "none");
  }

  public long getToolDistanceCovered() throws RequiredState {
    if (!processWasFinished(ProcessName.CUT)) throw new RequiredState(ProcessName.CUT);
    return stateEnterEvent.getParams().getToolDistanceCovered();
  }

  public int getToolAngle() throws RequiredState {
    if (!processWasFinished(ProcessName.CUT)) throw new RequiredState(ProcessName.CUT);
    return stateEnterEvent.getParams().getToolAngle();
  }

  public int getWheelDiameter() throws RequiredState {
    if (!processWasFinished(ProcessName.LOWE)) throw new RequiredState(ProcessName.LOWE);
    return stateEnterEvent.getParams().getWheelDiameter();
  }
}
