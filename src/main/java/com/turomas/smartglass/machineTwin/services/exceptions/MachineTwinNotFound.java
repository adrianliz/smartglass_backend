package com.turomas.smartglass.machineTwin.services.exceptions;

public class MachineTwinNotFound extends RuntimeException {
	public MachineTwinNotFound(String machineName) {
		super("Machine twin with name '" + machineName +"' was not found");
	}
}
