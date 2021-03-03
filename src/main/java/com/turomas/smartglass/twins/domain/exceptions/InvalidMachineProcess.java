package com.turomas.smartglass.twins.domain.exceptions;

import com.turomas.smartglass.events.domain.EventType;
import com.turomas.smartglass.events.domain.MachineEvent;

public class InvalidMachineProcess extends RuntimeException {
	public InvalidMachineProcess(MachineEvent event) {
		super("Event: " + event.getType() + " must be " + EventType.START_PROCESS);
	}
}
