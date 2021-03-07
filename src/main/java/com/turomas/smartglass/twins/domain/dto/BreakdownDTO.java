package com.turomas.smartglass.twins.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BreakdownDTO implements Comparable<BreakdownDTO> {
  private final String errorName;
  private final int timesOccurred;

  @Override
  public int compareTo(BreakdownDTO breakdown) {
    if (timesOccurred > breakdown.timesOccurred) {
      return 1;
    } else if (timesOccurred < breakdown.timesOccurred) {
      return -1;
    }
    return 0;
  }
}
