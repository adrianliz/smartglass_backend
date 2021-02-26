package com.turomas.smartglass.machineTwin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RatioDTO {
  private final RatioType ratio;
  private final double value;
  private final Period period;
}
