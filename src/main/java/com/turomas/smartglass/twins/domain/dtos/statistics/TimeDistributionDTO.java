package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.turomas.smartglass.twins.domain.dtos.statistics.ToHoursUtil.transformToHours;

@Getter
@EqualsAndHashCode
public class TimeDistributionDTO {
  private final long processingGlassHours;
  private final long loadingGlassHours;
  private final long standbyHours;

  public TimeDistributionDTO(long processingGlassSeconds, long loadingGlassSeconds, long standbySeconds) {
    processingGlassHours = transformToHours(processingGlassSeconds);
    loadingGlassHours = transformToHours(loadingGlassSeconds);
    standbyHours = transformToHours(standbySeconds);
  }
}
