package com.turomas.smartglass.twins.domain.statesmachine;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.guards.EndProcessMatcher;
import com.turomas.smartglass.twins.domain.statesmachine.inconsistencies.UpdatePreviousEvent;

import java.util.*;

public class StatesMachine {
  private TwinState currentState;
  private final Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions;
  private final Map<TransitionTrigger<TwinStateId, TwinStateId>, GuardStrategy> guards;
  private final Map<TransitionTrigger<TwinStateId, EventType>, InconsistencyStrategy> inconsistencies;

  public StatesMachine(TwinState initialState,
                       Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions) {
    this.currentState = initialState;
    this.transitions = transitions;

    guards = new HashMap<>();
    guards.put(new TransitionTrigger<>(TwinStateId.DOING_PROCESS, TwinStateId.IN_STANDBY),
               new EndProcessMatcher());

    inconsistencies = new HashMap<>();
    inconsistencies.put(
      new TransitionTrigger<>(TwinStateId.DOING_PROCESS, EventType.START_PROCESS),
      new UpdatePreviousEvent());
  }

  private void executeTransition(TwinStateId newStateId, Collection<TwinState> transitedStates) {
    currentState =
      new TwinState(newStateId, currentState.getTwinName(), getLastEventEvaluated());
    transitedStates.add(currentState);
  }

  private void processEvent(Event event, Collection<TwinState> transitedStates) {
    if (event != null) {
      currentState.updateLastEventEvaluated(event);

      TwinStateId newStateId =
        transitions.get(new TransitionTrigger<>(currentState.getTwinStateId(), event.getType()));
      GuardStrategy guard = guards.get(new TransitionTrigger<>(currentState.getTwinStateId(), newStateId));
      InconsistencyStrategy inconsistency =
        inconsistencies.get(new TransitionTrigger<>(currentState.getTwinStateId(), event.getType()));

      if (inconsistency != null) {
        inconsistency.fixInconsistency(currentState.getEnterEvent(), event);
      } else if (guard != null) {
        if (! guard.cutTransition(currentState.getEnterEvent(), event)) {
          executeTransition(newStateId, transitedStates);
        }
      } else if (newStateId != null) {
        executeTransition(newStateId, transitedStates);
      }
    }
  }

  public Collection<TwinState> processEvents(Collection<Event> events) {
    SortedSet<TwinState> transitedStates = new TreeSet<>();
    transitedStates.add(currentState);

    for (Event event : events) {
      processEvent(event, transitedStates);
    }

    return transitedStates;
  }

  public Event getLastEventEvaluated() {
    return currentState.getLastEventEvaluated();
  }

  public TwinStateId getCurrentState() {
    return currentState.getTwinStateId();
  }
}
