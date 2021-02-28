package com.turomas.smartglass.machineTwin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatioDTO {
  private final RatioType ratio;
  private final double value;
}
