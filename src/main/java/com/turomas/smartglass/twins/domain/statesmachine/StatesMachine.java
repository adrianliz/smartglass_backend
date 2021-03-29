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

	private void executeTransition(TwinStateId newStateId, List<TwinState> statesTransited) {
		currentState =
			new TwinState(newStateId, currentState.getTwinName(), getLastEventEvaluated());
		statesTransited.add(currentState);
	}

	private void processEvent(Event event, List<TwinState> statesTransited) {
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
					executeTransition(newStateId, statesTransited);
				}
			} else if (newStateId != null) {
				executeTransition(newStateId, statesTransited);
			}
		}
	}

	public List<TwinState> processEvents(SortedSet<Event> events) {
		List<TwinState> statesTransited = new ArrayList<>();
		statesTransited.add(currentState);

		for (Event event : events) {
			processEvent(event, statesTransited);
		}

		return statesTransited;
	}

	public Event getLastEventEvaluated() {
		return currentState.getLastEventEvaluated();
	}
}
