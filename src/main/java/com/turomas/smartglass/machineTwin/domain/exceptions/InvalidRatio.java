package com.turomas.smartglass.machineTwin.domain.exceptions;

import com.turomas.smartglass.machineTwin.domain.RatioType;

public class InvalidRatio extends RuntimeException {
  public InvalidRatio(RatioType ratio) {
    super(
        "Ratio must be: '"
            + RatioType.AVAILABILITY
            + "', '"
            + RatioType.EFFICIENCY
            + "' or '"
            + RatioType.EFFECTIVENESS
            + "'");
  }
}
