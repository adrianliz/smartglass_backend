package com.turomas.smartglass.twins.domain.exceptions;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.ProcessName;
import com.turomas.smartglass.twins.domain.statesmachine.TwinStateId;

public class RequiredCondition extends RuntimeException {
	public RequiredCondition(TwinStateId stateId) {
		super("State must be '" + stateId + "'");
	}

	public RequiredCondition(ProcessName name) {
		super(
			"Previous state must be '"
				+ TwinStateId.DOING_PROCESS
				+ "' doing process name '"
				+ name
				+ "' and current state must be '"
				+ TwinStateId.IN_STANDBY
				+ "'");
	}

	public RequiredCondition(EventType enterEvent) {
		super("State enter event must be '" + enterEvent + "'");
	}
}
