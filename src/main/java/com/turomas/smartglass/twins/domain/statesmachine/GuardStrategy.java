package com.turomas.smartglass.twins.domain.statesmachine;

import com.turomas.smartglass.events.domain.Event;

public interface GuardStrategy {
	boolean cutTransition(Event startProcessEvent, Event endProcessEvent);
}
