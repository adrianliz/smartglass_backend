package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TransitionTrigger;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;

import java.util.Map;

public class StateMachinesMother {
  public static StatesMachine create(TwinState initialState,
                                     Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions) {
    return new StatesMachine(initialState, transitions);
  }

  public static StatesMachine create(String twinName, TwinState initialState,
                                     Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions) {
    return new StatesMachine(initialState, transitions);
  }
}
