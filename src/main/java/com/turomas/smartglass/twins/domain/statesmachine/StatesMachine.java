package com.turomas.smartglass.twins.domain.statesmachine;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.exceptions.InvalidInitialState;
import com.turomas.smartglass.twins.domain.statesmachine.guards.GuardStrategy;
import com.turomas.smartglass.twins.domain.statesmachine.guards.ParamsMatcher;

import java.util.*;

public class StatesMachine {
  private TwinState currentState;
  private final String twinName;
  private final Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions;
  private final Map<TransitionTrigger<TwinStateId, TwinStateId>, GuardStrategy> guards;

  public StatesMachine(TwinState initialState,
                       Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions)
    throws InvalidInitialState {

    if (initialState == null) throw new InvalidInitialState();

    this.twinName = initialState.getTwinName();
    this.currentState = initialState;
    this.transitions = transitions;

    guards = new HashMap<>();
    guards.put(new TransitionTrigger<>(TwinStateId.DOING_PROCESS, TwinStateId.IN_STANDBY),
               new ParamsMatcher());
  }

  private void executeTransition(TwinStateId newStateId, Event lastEventEvaluated,
                                 Collection<TwinState> transitedStates) {
    currentState =
      TwinState.of(newStateId, twinName, lastEventEvaluated);
    transitedStates.add(currentState);
  }

  private void processEvent(Event event, Collection<TwinState> transitedStates) {
    if (event != null) {
      currentState.updateLastEventEvaluated(event);

      TwinStateId newStateId =
        transitions.get(new TransitionTrigger<>(getCurrentStateId(), event.getType()));
      GuardStrategy guard = guards.get(new TransitionTrigger<>(getCurrentStateId(), newStateId));

      if (guard != null) {
        if (! guard.cutTransition(currentState)) {
          executeTransition(newStateId, event, transitedStates);
        }
      } else if (newStateId != null) {
        executeTransition(newStateId, event, transitedStates);
      }
    }
  }

  public Collection<TwinState> processEvents(EventsService eventsService) {
    SortedSet<TwinState> transitedStates = new TreeSet<>();
    transitedStates.add(currentState);

    if (transitions != null) {
      for (Event event : currentState.getSubsequentEvents(eventsService)) {
        processEvent(event, transitedStates);
      }
    }

    return transitedStates;
  }

  public TwinStateId getCurrentStateId() {
    return currentState.getTwinStateId();
  }
}
