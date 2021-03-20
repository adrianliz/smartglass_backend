package com.turomas.smartglass.twins.domain.statesmachine;

import com.turomas.smartglass.events.domain.Event;

public interface InconsistencyStrategy {
	void fixInconsistency(Event previousEvent, Event currentEvent);
}
