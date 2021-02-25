package com.turomas.smartglass.machineTwin.domain.exceptions;

import java.time.LocalDateTime;

public class PeriodNotValidException extends RuntimeException {
  public PeriodNotValidException(LocalDateTime startDate, LocalDateTime endDate) {
    super("End date '" + startDate + "' must be after start date '" + endDate + "'");
  }
}
