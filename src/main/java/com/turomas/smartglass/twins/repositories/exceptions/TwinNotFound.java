package com.turomas.smartglass.twins.repositories.exceptions;

public class TwinNotFound extends RuntimeException {
	public TwinNotFound(String machineName) {
		super("Twin with name '" + machineName + "' was not found");
	}
}
