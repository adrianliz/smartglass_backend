package com.turomas.smartglass.twins.domain.exceptions;

public class InvalidInitialState extends RuntimeException {
  public InvalidInitialState() {
    super("States machine must have an initial state");
  }
}
