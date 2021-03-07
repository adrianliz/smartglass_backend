package com.turomas.smartglass.twins.domain;

import com.turomas.smartglass.events.domain.MachineEvent;

public enum StateType {
  DOING_PROCESS {
    @Override
    public StateType doTransition(MachineEvent event, MachineEvent stateEnterEvent) {
      if (event.machineIsInBreakdown()) return IN_BREAKDOWN;
      else if (event.machineCompletesProcess(stateEnterEvent)) return IN_STANDBY;
      return this;
    }
  },
  IN_BREAKDOWN {
    @Override
    public StateType doTransition(MachineEvent event, MachineEvent stateEnterEvent) {
      if (event.machineStartsProcess()) return DOING_PROCESS;
      else if (event.machineIsRearmed()) return IN_STANDBY;
      return this;
    }
  },
  IN_STANDBY {
    @Override
    public StateType doTransition(MachineEvent event, MachineEvent stateEnterEvent) {
      if (event.machineStartsProcess()) return DOING_PROCESS;
      else if (event.machineIsInBreakdown()) return IN_BREAKDOWN;
      return this;
    }
  };

  public abstract StateType doTransition(MachineEvent event, MachineEvent stateEnterEvent);
}
