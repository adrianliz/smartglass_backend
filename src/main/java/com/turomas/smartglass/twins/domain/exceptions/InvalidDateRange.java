package com.turomas.smartglass.twins.domain.exceptions;

import java.time.LocalDateTime;

public class InvalidDateRange extends RuntimeException {
  public InvalidDateRange() {
    super("Start date and end date must not be null");
  }

  public InvalidDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    super("End date '" + endDate + "' must be after start date '" + startDate + "'");
  }
}
