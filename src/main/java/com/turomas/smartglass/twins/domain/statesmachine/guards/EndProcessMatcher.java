package com.turomas.smartglass.twins.domain.statesmachine.guards;

import com.turomas.smartglass.events.domain.Event;
import com.turomas.smartglass.twins.domain.statesmachine.GuardStrategy;

public class EndProcessMatcher implements GuardStrategy {
	public boolean cutTransition(Event startProcessEvent, Event endProcessEvent) {
		return ((startProcessEvent == null)
		        || (endProcessEvent == null)
		        || (! startProcessEvent.isFinalizedBy(endProcessEvent)));
	}
}
