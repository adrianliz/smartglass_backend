package com.turomas.smartglass.twins.domain.dto;

import lombok.Getter;

@Getter
public class RatioDTO {
  private final RatioType ratio;
  private final double value;

  public RatioDTO(RatioType ratio, long firstMeasure, long secondMeasure) {
    this.ratio = ratio;
    long denominator = firstMeasure + secondMeasure;

    if (denominator > 0) {
      value = (double) firstMeasure / denominator;
    } else {
      value = Double.MAX_VALUE; // Preguntar
    }
  }
}
