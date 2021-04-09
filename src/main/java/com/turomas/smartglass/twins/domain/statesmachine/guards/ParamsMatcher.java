package com.turomas.smartglass.twins.domain.statesmachine.guards;

import com.turomas.smartglass.twins.domain.statesmachine.TwinState;

public class ParamsMatcher implements GuardStrategy {
  public boolean cutTransition(TwinState currentState) {
    return ((currentState == null) || (! currentState.eventParamsMatch()));
  }
}
