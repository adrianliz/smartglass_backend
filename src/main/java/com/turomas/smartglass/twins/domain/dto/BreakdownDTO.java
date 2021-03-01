package com.turomas.smartglass.twins.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BreakdownDTO {
  private final String errorName;
  private final int timesOccurred;
}
