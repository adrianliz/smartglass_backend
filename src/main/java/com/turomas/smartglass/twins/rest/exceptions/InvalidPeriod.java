package com.turomas.smartglass.twins.rest.exceptions;

import com.turomas.smartglass.twins.domain.Period;

import java.util.Arrays;

public class InvalidPeriod extends RuntimeException {
  public InvalidPeriod() {
    super("Period must be one of '" + Arrays.toString(Period.values()) + "'");
  }
}
