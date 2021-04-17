package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class TimeDistributionDTO {
  private final long processingGlassSeconds;
  private final long loadingGlassSeconds;
  private final long standbySeconds;
}
