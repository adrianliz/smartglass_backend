package com.turomas.smartglass.twins;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateType;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class TransitionsMother {
  public static Map<Pair<TwinStateType, EventType>, TwinStateType> createOneTransition(
    Pair<TwinStateType, EventType> trigger, TwinStateType newStateId) {

    Map<Pair<TwinStateType, EventType>, TwinStateType> defaultTransition = new HashMap<>();
    defaultTransition.put(trigger, newStateId);

    return defaultTransition;
  }
}
