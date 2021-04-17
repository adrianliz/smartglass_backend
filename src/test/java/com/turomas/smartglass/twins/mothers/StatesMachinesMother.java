package com.turomas.smartglass.twins.mothers;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.StatesMachine;
import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import org.springframework.data.util.Pair;

import java.util.Map;

public class StatesMachinesMother {
  public static StatesMachine create(TwinState initialState,
                                     Map<Pair<TwinStateType, EventType>, TwinStateType> transitions) {
    return new StatesMachine(initialState, transitions);
  }
}
