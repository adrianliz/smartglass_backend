package com.turomas.smartglass.twins.domain.statesmachine.inconsistencies;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.twins.domain.statesmachine.InconsistencyStrategy;

public class UpdatePreviousEvent implements InconsistencyStrategy {
	public void fixInconsistency(Event previousEvent, Event currentEvent) {
		if ((previousEvent != null) && (currentEvent != null)) {
			previousEvent.updateStartEvent(currentEvent);
		}
	}
}
