package com.turomas.smartglass.services.exceptions;

public class MachineEventNotFound extends RuntimeException {
  public MachineEventNotFound(String eventId) {
    super("Machine event with ID '" + eventId + "' was not found");
  }
}
