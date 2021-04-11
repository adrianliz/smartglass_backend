package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class RatioDTO {
  public enum RatioId {
    AVAILABILITY,
    EFFICIENCY,
    EFFECTIVENESS
  }

  @NonNull
  private final RatioId ratio;
  private final double value;

  public RatioDTO(RatioId ratio, long firstMeasure, long secondMeasure) {
    this.ratio = ratio;
    long denominator = firstMeasure + secondMeasure;

    if ((denominator > 0) && (firstMeasure > 0)) {
      value = (double) firstMeasure / denominator;
    } else {
      value = 0;
    }
  }
}
