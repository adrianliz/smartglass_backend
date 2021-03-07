package com.turomas.smartglass.twins.rest.exceptions;

import com.turomas.smartglass.twins.domain.Period;

import java.util.Arrays;

public class InvalidPeriodType extends RuntimeException {
  public InvalidPeriodType() {
    super("Period type must be one of '" + Arrays.toString(Period.values()) + "'");
  }
}
