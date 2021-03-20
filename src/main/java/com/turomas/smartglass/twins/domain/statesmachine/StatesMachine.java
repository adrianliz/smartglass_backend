package com.turomas.smartglass.twins.domain.statesmachine;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.twins.domain.statesmachine.guards.EndProcessMatcher;
import com.turomas.smartglass.twins.domain.statesmachine.inconsistencies.UpdatePreviousEvent;

import java.util.HashMap;
import java.util.Map;

public class StatesMachine {
	private TwinStateId currentState;
	private Event enterEvent;
	private final Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions;
	private final Map<TransitionTrigger<TwinStateId, TwinStateId>, GuardStrategy> guards;
	private final Map<TransitionTrigger<TwinStateId, EventType>, InconsistencyStrategy> inconsistencies;

	public StatesMachine(Map<TransitionTrigger<TwinStateId, EventType>, TwinStateId> transitions) {
		this.transitions = transitions;
		currentState = TwinStateId.OFF;

		guards = new HashMap<>();
		guards.put(new TransitionTrigger<>(TwinStateId.DOING_PROCESS, TwinStateId.IN_STANDBY), new EndProcessMatcher());

		inconsistencies = new HashMap<>();
		inconsistencies.put(
			new TransitionTrigger<>(TwinStateId.DOING_PROCESS, EventType.START_PROCESS),
			new UpdatePreviousEvent());
	}

	public TwinStateId getCurrentState() {
		return currentState;
	}

	public TwinStateId doTransition(Event event) {
		if (event != null) {
			TwinStateId newState = transitions.get(new TransitionTrigger<>(currentState, event.getType()));
			GuardStrategy guard = guards.get(new TransitionTrigger<>(currentState, newState));
			InconsistencyStrategy inconsistency = inconsistencies.get(new TransitionTrigger<>(currentState, event.getType()));

			if (inconsistency != null) {
				inconsistency.fixInconsistency(enterEvent, event);
			} else if (guard != null) {
				if (! guard.cutTransition(enterEvent, event)) {
					currentState = newState;
					enterEvent = event;
				}
			} else if (newState != null) {
				currentState = newState;
				enterEvent = event;
			}
		}

		return currentState;
	}
}
