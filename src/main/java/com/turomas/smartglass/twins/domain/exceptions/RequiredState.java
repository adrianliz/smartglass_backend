package com.turomas.smartglass.twins.domain.exceptions;

import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.StateType;

public class RequiredState extends RuntimeException {
  public RequiredState(StateType type) {
    super("State must be '" + type + "'");
  }

  public RequiredState(ProcessName name) {
    super(
        "Previous state must be '"
            + StateType.DOING_PROCESS
            + "' doing process name '"
            + name
            + "' and current state must be '"
            + StateType.IN_STANDBY
            + "'");
  }
}
