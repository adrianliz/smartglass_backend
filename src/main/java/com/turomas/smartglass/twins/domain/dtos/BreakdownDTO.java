package com.turomas.smartglass.twins.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BreakdownDTO implements Comparable<BreakdownDTO> {
  @EqualsAndHashCode.Include
  private final String cause;
  private final long timesOccurred;

  @Override
  public int compareTo(BreakdownDTO breakdown) {
    if (this.equals(breakdown)) return 0;
    if (timesOccurred > breakdown.timesOccurred) {
      return 1;
    }
    return - 1;
  }
}
