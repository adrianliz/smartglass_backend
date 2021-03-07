package com.turomas.smartglass.twins.domain.dto;

import lombok.Getter;

@Getter
public class WorkingHoursDTO {
  static final int SECONDS_TO_HOURS_FACTOR = 120;

  private final long workingHours;
  private final long activeHours;

  public WorkingHoursDTO(long workingSeconds, long activeSeconds) {
    workingHours = workingSeconds / SECONDS_TO_HOURS_FACTOR;
    activeHours = activeSeconds / SECONDS_TO_HOURS_FACTOR;
  }
}
