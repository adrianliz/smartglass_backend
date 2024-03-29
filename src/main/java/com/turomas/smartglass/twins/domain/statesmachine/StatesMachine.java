package com.turomas.smartglass.twins.domain.statesmachine;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.services.EventsService;
import com.turomas.smartglass.twins.domain.exceptions.InvalidInitialState;
import com.turomas.smartglass.twins.domain.statesmachine.guards.GuardStrategy;
import com.turomas.smartglass.twins.domain.statesmachine.guards.ParamsMatcher;
import lombok.NonNull;
import org.springframework.data.util.Pair;

import java.util.*;

public class StatesMachine {
  @NonNull
  private final String twinName;
  private final Map<Pair<TwinStateType, EventType>, TwinStateType> transitions;
  @NonNull
  private final Map<Pair<TwinStateType, TwinStateType>, GuardStrategy> guards;
  @NonNull
  private TwinState currentState;

  public StatesMachine(TwinState initialState, Map<Pair<TwinStateType, EventType>, TwinStateType> transitions)
    throws InvalidInitialState {

    if (initialState == null) throw new InvalidInitialState();

    this.twinName = initialState.getTwinName();
    this.transitions = transitions;
    this.currentState = initialState;

    guards = new HashMap<>();
    guards.put(Pair.of(TwinStateType.DOING_PROCESS, TwinStateType.IN_STANDBY), new ParamsMatcher());
  }

  private void processEvent(Event event, Collection<TwinState> transitedStates) {
    currentState.createTransitionTriggerFor(event).ifPresent(transitionTrigger -> {
      TwinStateType newStateType = transitions.get(transitionTrigger);

      if (newStateType != null) {
        GuardStrategy guard = guards.get(Pair.of(currentState.getType(), newStateType));

        if ((guard == null) || (! guard.cutTransition(currentState))) {
          currentState = TwinState.with(newStateType, twinName, event);
          transitedStates.add(currentState);
        }
      }
    });
  }

  public SortedSet<TwinState> processEvents(EventsService eventsService) {
    SortedSet<TwinState> transitedStates = new TreeSet<>();
    transitedStates.add(currentState);

    if (transitions != null) {
      for (Event event : currentState.getSubsequentEvents(eventsService)) {
        processEvent(event, transitedStates);
      }
    }

    return transitedStates;
  }
}
