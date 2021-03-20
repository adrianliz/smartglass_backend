package com.turomas.smartglass.twins.domain.dto;

import lombok.Getter;

@Getter
public class RatioDTO {
  public enum RatioId {
    AVAILABILITY,
    EFFICIENCY,
    EFFECTIVENESS
  }

  private final RatioId ratio;
  private final double value;

  public RatioDTO(RatioId ratio, long firstMeasure, long secondMeasure) {
    this.ratio = ratio;
    long denominator = firstMeasure + secondMeasure;

    if (denominator > 0) {
      value = (double) firstMeasure / denominator;
    } else {
      value = 0;
    }
  }
}
