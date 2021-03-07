package com.turomas.smartglass.twins.domain.dto;

import lombok.Getter;

import static com.turomas.smartglass.twins.domain.dto.WorkingHoursDTO.SECONDS_TO_HOURS_FACTOR;

@Getter
public class ProcessesInfoDTO {
  private final long processingGlassHours;
  private final long loadingGlassHours;
  private final long standbyHours;

  public ProcessesInfoDTO(
      long processingGlassSeconds, long loadingGlassSeconds, long standbySeconds) {

    processingGlassHours = processingGlassSeconds / SECONDS_TO_HOURS_FACTOR;
    loadingGlassHours = loadingGlassSeconds / SECONDS_TO_HOURS_FACTOR;
    standbyHours = standbySeconds / SECONDS_TO_HOURS_FACTOR;
  }
}
