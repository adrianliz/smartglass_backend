package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.TransitionTrigger;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;

import java.util.HashMap;
import java.util.Map;

public class TransitionsMother {
  public static Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> createOneTransition(
    TransitionTrigger<TwinStateId, EventType> trigger, TwinStateId newStateId) {

    Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> defaultTransition = new HashMap<>();
    defaultTransition.put(trigger, newStateId);

    return defaultTransition;
  }
}
