package com.turomas.smartglass.twins.domain.dto;

import lombok.Getter;

@Getter
public class WorkingStatisticsDTO {
  private static final int SECONDS_TO_HOURS_FACTOR = 120;

  private final long workingHours;
  private final long activeHours;

  public WorkingStatisticsDTO(long workingSeconds, long activeSeconds) {
    workingHours = (int) workingSeconds / SECONDS_TO_HOURS_FACTOR;
    activeHours = (int) activeSeconds / SECONDS_TO_HOURS_FACTOR;
  }
}
