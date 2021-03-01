package com.turomas.smartglass.twins.services.exceptions;

public class MachineTwinNotFound extends RuntimeException {
  public MachineTwinNotFound(String machineName) {
    super("Machine twin with name '" + machineName + "' was not found");
  }
}
