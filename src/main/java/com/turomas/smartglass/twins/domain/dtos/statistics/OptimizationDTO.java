package com.turomas.smartglass.twins.domain.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OptimizationDTO implements Comparable<OptimizationDTO> {
  @EqualsAndHashCode.Include
  @NonNull
  private final String name;
  @EqualsAndHashCode.Include
  @NonNull
  private final String material;
  private final long piecesProcessed;

  @Override
  public int compareTo(OptimizationDTO optimization) {
    if (this.equals(optimization)) return 0;
    if (piecesProcessed > optimization.piecesProcessed) {
      return 1;
    }
    return - 1;

  }
}
